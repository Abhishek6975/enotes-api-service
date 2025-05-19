package com.koyta.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koyta.dto.FavouriteNotesDto;
import com.koyta.dto.NotesDto;
import com.koyta.dto.NotesDto.CategoryDto;
import com.koyta.dto.NotesDto.FilesDto;
import com.koyta.dto.NotesResponse;
import com.koyta.entity.FavouriteNotes;
import com.koyta.entity.FilesDetails;
import com.koyta.entity.Notes;
import com.koyta.exception.NoteSaveException;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.repository.CategoryRepository;
import com.koyta.repository.FavouriteNotesRepository;
import com.koyta.repository.FileRepository;
import com.koyta.repository.NotesRepository;
import com.koyta.service.NotesService;
import com.koyta.util.CommonUtil;

@Service
public class NotesServiceImpl implements NotesService {

	private NotesRepository notesRepository;

	private ModelMapper modelMapper;

	private CategoryRepository categoryRepository;

	private FileRepository fileRepository;

	private FavouriteNotesRepository favouriteNotesRepository;

	@Value("${file.upload.path}")
	private String uploadPath;

	public NotesServiceImpl(NotesRepository notesRepository, ModelMapper modelMapper,
			CategoryRepository categoryRepository, FileRepository fileRepository,
			FavouriteNotesRepository favouriteNotesRepository) {
		super();
		this.notesRepository = notesRepository;
		this.modelMapper = modelMapper;
		this.categoryRepository = categoryRepository;
		this.fileRepository = fileRepository;
		this.favouriteNotesRepository = favouriteNotesRepository;
	}

	@Override
	public Boolean saveNotes(String notes,  MultipartFile file) throws ResourceNotFoundException ,NoteSaveException {
		
		ObjectMapper objectMapper = new ObjectMapper();

		try {

			NotesDto notesDto = objectMapper.readValue(notes, NotesDto.class);
			notesDto.setIsDeleted(false);
			notesDto.setDeletedOn(null);

			// Update if id is given in Request
			if (!ObjectUtils.isEmpty(notesDto.getId())) {

				updateNotes(notesDto, file);
			}

			// Validation Notes Checking

			checkCategoryExist(notesDto.getCategory());

			Notes notesMap = modelMapper.map(notesDto, Notes.class);

			FilesDetails filesDetails = saveFileDetails(file);

			if (!ObjectUtils.isEmpty(filesDetails)) {

				notesMap.setFilesDetails(filesDetails);

			} else {

				if (ObjectUtils.isEmpty(notesDto.getId())) {

					notesMap.setFilesDetails(null);
				}
			}

			Notes saveNotes = notesRepository.save(notesMap);

			if (!ObjectUtils.isEmpty(saveNotes)) {

				return true;
			}

		} catch (JsonProcessingException e) {
			throw new NoteSaveException("Invalid note format", e);
		} catch (IllegalAccessException | IOException e) {
			throw new NoteSaveException("File handling failed", e);
		}
		return false;

	}

	private void updateNotes(NotesDto notesDto, MultipartFile file) throws ResourceNotFoundException {
		
		Notes exitsNotes = notesRepository.findById(notesDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Note not found for update. ID: " + notesDto.getId()));
		
		
		//user not choose any file at notes update time
		if(ObjectUtils.isEmpty(file) && !ObjectUtils.isEmpty(exitsNotes.getFilesDetails())){
			
			notesDto.setFilesDetails(modelMapper.map(exitsNotes.getFilesDetails(), FilesDto.class));
			
		}
	}

	private FilesDetails saveFileDetails(MultipartFile file) throws IOException, IllegalAccessException {
		
		if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {

			String originalFileName = file.getOriginalFilename();
			String extension = FilenameUtils.getExtension(originalFileName);

			List<String> extensionAllow = Arrays.asList("pdf", "xlsx", "jpg", "png", "java");

			if (!extensionAllow.contains(extension)) {

				throw new IllegalAccessException("invalid file Formate ! Upload only .pdf, .xlsx, .jpg, .png, .java");

			}

			String randomString = UUID.randomUUID().toString();

			String uploadFileName = randomString + "." + extension;


			File saveFile = new File(uploadPath);

			if (!saveFile.exists()) {

				saveFile.mkdir();
			}

			// path : Enotes-API-Service/notes/java.pdf

			String storePath = uploadPath.concat(uploadFileName);


			// upload file
			long upload = Files.copy(file.getInputStream(), Paths.get(storePath));

			if (upload != 0) {

				FilesDetails fileDetails = new FilesDetails();

				fileDetails.setOriginalFileName(originalFileName);
				fileDetails.setDisplayFileName(getDisplayName(originalFileName)); // getDisplayName Method()
				fileDetails.setUploadFileName(uploadFileName);
				fileDetails.setFileSize(file.getSize());
				fileDetails.setPath(storePath);

				return fileRepository.save(fileDetails);
			}

		}
		return null;
		
	}

	private String getDisplayName(String originalFileName) {
		
		// java_programming_tutorial.pdf  => java_prog.pdf
		
		String extension = FilenameUtils.getExtension(originalFileName);
		
		String fileName = FilenameUtils.removeExtension(originalFileName);
		
		if(fileName.length() > 8) {
			
			fileName = fileName.substring(0, 7);
			
		}
		fileName+="."+extension;
		
		return fileName;
		
	}

	private void checkCategoryExist(CategoryDto category) throws ResourceNotFoundException {

		categoryRepository.findById(category.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Category id invalid"));

	}

	@Override
	public List<NotesDto> getAllNotes() {

		return notesRepository.findAll().stream().map(note -> modelMapper.map(note, NotesDto.class)).toList();

	}

	@Override
	public byte[] downloadFile(FilesDetails filesDetails) throws ResourceNotFoundException, IOException {

		InputStream io = new FileInputStream(filesDetails.getPath());

		return StreamUtils.copyToByteArray(io);

	}

	@Override
	public FilesDetails getFileDetails(Integer id) throws ResourceNotFoundException {

		return fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File Not Found"));
	}

	@Override
	public NotesResponse getAllNotesByUser(Integer pageNo, Integer pageSize) {
		
		Integer userId = CommonUtil.getLoggedInUser().getId();
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);

		Page<Notes> pageNotes = notesRepository.findByCreatedByAndIsDeletedFalse(userId, pageable);

		List<NotesDto> notesDtos = pageNotes.get().map(n -> modelMapper.map(n, NotesDto.class)).toList();

		return NotesResponse.builder()
				.notes(notesDtos)
				.pageNo(pageNotes.getNumber())
				.pageSize(pageNotes.getSize())
				.totalElements(pageNotes.getTotalElements())
				.totalPages(pageNotes.getTotalPages())
				.isFirst(pageNotes.isFirst())
				.isLast(pageNotes.isLast())
				.build();

	}

	@Override
	public void softDeleteNotes(Integer id) throws ResourceNotFoundException {

		Notes notes = notesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes Id invalid! Not Found"));
		
		notes.setIsDeleted(true);
		notes.setDeletedOn(LocalDateTime.now());
		notesRepository.save(notes);
	}

	@Override
	public void restoreNotes(Integer id) throws ResourceNotFoundException {
		Notes notes = notesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes Id invalid! Not Found"));
		
		notes.setIsDeleted(false);
		notes.setDeletedOn(null);
		notesRepository.save(notes);
		
		
	}

	@Override
	public List<NotesDto> getUserRecycleBinNotes() throws ResourceNotFoundException {

		Integer userId = CommonUtil.getLoggedInUser().getId();

		List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);

		if (recycleNotes.isEmpty()) {
			throw new ResourceNotFoundException("No notes found in recycle bin for user with ID: " + userId);
		}

		return recycleNotes.stream().map(notes -> modelMapper.map(notes, NotesDto.class)).toList();
	}

	@Override
	public void hardDeleteNotes(Integer id) throws ResourceNotFoundException {

		Notes notes = notesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes Not Found"));

		if (notes.getIsDeleted()) {
			notesRepository.delete(notes);
		} else {
			throw new IllegalArgumentException("Sorry You Can't hard Delete Direclty");
		}

	}

	@Override
	public void emptyRecycleBin() throws ResourceNotFoundException {

		Integer userId = CommonUtil.getLoggedInUser().getId();

		List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);

		if (CollectionUtils.isEmpty(recycleNotes)) {

			throw new ResourceNotFoundException("Recycle bin is already empty for user with ID: " + userId);
		}

		notesRepository.deleteAll(recycleNotes);

	}

	@Override
	public void favouriteNotes(Integer noteId) throws ResourceNotFoundException {

		Integer userId = 1;

		Notes notes = notesRepository.findById(noteId)
				.orElseThrow(() -> new ResourceNotFoundException("Notes id Invalid"));

		FavouriteNotes favouriteNotes = FavouriteNotes.builder()
				.notes(notes)
				.userId(userId)
				.build();

		favouriteNotesRepository.save(favouriteNotes);

	}

	@Override
	public void unFavouriteNotes(Integer favouriteNoteId) throws ResourceNotFoundException {
		

		 FavouriteNotes favNote = favouriteNotesRepository.findById(favouriteNoteId)
				.orElseThrow(() -> new ResourceNotFoundException("Favourite Note Not Found"));
		
		favouriteNotesRepository.delete(favNote);
		
	}

	@Override
	public List<FavouriteNotesDto> getUserFavouriteNotes() {
		
		Integer userId = CommonUtil.getLoggedInUser().getId();
		List<FavouriteNotes> favNotes = favouriteNotesRepository.findByUserId(userId);

		return favNotes.stream().map((FavouriteNotes favN) -> modelMapper.map(favN, FavouriteNotesDto.class))
				.toList();

	}

	@Override
	public Boolean copyNotes(Integer id) throws ResourceNotFoundException {

		Notes notes = notesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes id is Invalid"));

		Notes cpyNotes = Notes.builder()
				.title(notes.getTitle())
				.description(notes.getDescription())
				.category(notes.getCategory())
				.isDeleted(false)
				.filesDetails(null)
				.build();

		Notes save = notesRepository.save(cpyNotes);

		return !ObjectUtils.isEmpty(save);

	}

	@Override
	public NotesResponse getNotesByUserSearch(Integer pageNo, Integer pageSize, String keyword) {

		Integer userId = CommonUtil.getLoggedInUser().getId();

		Pageable pageable = PageRequest.of(pageNo, pageSize);

		Page<Notes> pageNotes = notesRepository.searchNotes(keyword, userId, pageable);

		List<NotesDto> notesDtos = pageNotes.get().map(n -> modelMapper.map(n, NotesDto.class)).toList();

		return NotesResponse.builder()
				.notes(notesDtos)
				.pageNo(pageNotes.getNumber())
				.pageSize(pageNotes.getSize())
				.totalElements(pageNotes.getTotalElements())
				.totalPages(pageNotes.getTotalPages())
				.isFirst(pageNotes.isFirst())
				.isLast(pageNotes.isLast())
				.build();

	}

}
