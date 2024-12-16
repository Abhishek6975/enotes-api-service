package com.koyta.service.Impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.catalina.mapper.Mapper;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koyta.dto.FavouriteNotesDto;
import com.koyta.dto.NotesDto;
import com.koyta.dto.NotesDto.CategoryDto;
import com.koyta.dto.NotesDto.FilesDto;
import com.koyta.dto.NotesResponse;
import com.koyta.entity.FavouriteNotes;
import com.koyta.entity.FilesDetails;
import com.koyta.entity.Notes;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.repository.CategoryRepository;
import com.koyta.repository.FavouriteNotesRepository;
import com.koyta.repository.FileRepository;
import com.koyta.repository.NotesRepository;
import com.koyta.service.NotesService;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private FavouriteNotesRepository favouriteNotesRepository;
	
	@Value("${file.upload.path}")
	private String uploadPath;

	@Override
	public Boolean saveNotes(String notes,  MultipartFile file) throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();

		NotesDto notesDto = objectMapper.readValue(notes, NotesDto.class);
		notesDto.setIsDeleted(false);
		notesDto.setDeletedOn(null);
		
		//Update if id is given in Request
		if(!ObjectUtils.isEmpty(notesDto.getId())) {
			
			updateNotes(notesDto, file);		
		}

		// Validation Notes Checking

		checkCategoryExist(notesDto.getCategory());

		Notes notesMap = modelMapper.map(notesDto, Notes.class);

		FilesDetails filesDetails = saveFileDetails(file);

		if (!ObjectUtils.isEmpty(filesDetails)) {

			notesMap.setFilesDetails(filesDetails);

		} else {
			
			if(ObjectUtils.isEmpty(notesDto.getId())) {
				
				notesMap.setFilesDetails(null);		
			}
		}

		Notes saveNotes = notesRepository.save(notesMap);

		if (!ObjectUtils.isEmpty(saveNotes)) {

			return true;
		}

		return false;

	}

	private void updateNotes(NotesDto notesDto, MultipartFile file) throws Exception {
		
		Notes exitsNotes = notesRepository.findById(notesDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Notes Id"));
		
		
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

				FilesDetails saveFileDtls = fileRepository.save(fileDetails);

				return saveFileDtls;

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

	private void checkCategoryExist(CategoryDto category) throws Exception {

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
	public FilesDetails getFileDetails(Integer id) throws Exception {

		FilesDetails filesDetails = fileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("File Not Found"));
		return filesDetails;
	}

	@Override
	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize) {
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);

		Page<Notes> pageNotes = notesRepository.findByCreatedByAndIsDeletedFalse(userId, pageable);

		List<NotesDto> notesDtos = pageNotes.get().map(n -> modelMapper.map(n, NotesDto.class)).toList();

		NotesResponse notes = NotesResponse.builder()
				.notes(notesDtos)
				.pageNo(pageNotes.getNumber())
				.pageSize(pageNotes.getSize())
				.totalElements(pageNotes.getTotalElements())
				.totalPages(pageNotes.getTotalPages())
				.isFirst(pageNotes.isFirst())
				.isLast(pageNotes.isLast())
				.build();
		
		
		return notes;
	}

	@Override
	public void softDeleteNotes(Integer id) throws Exception {

		Notes notes = notesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes Id invalid! Not Found"));
		
		notes.setIsDeleted(true);
		notes.setDeletedOn(LocalDateTime.now());
		notesRepository.save(notes);
	}

	@Override
	public void restoreNotes(Integer id) throws Exception {
		Notes notes = notesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes Id invalid! Not Found"));
		
		notes.setIsDeleted(false);
		notes.setDeletedOn(null);
		notesRepository.save(notes);
		
		
	}

	@Override
	public List<NotesDto> getUserRecycleBinNotes(Integer userId) {
		
		List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);

		List<NotesDto> notesDto = recycleNotes.stream().map(notes -> modelMapper.map(notes, NotesDto.class))
				.toList();

		return notesDto;
	}

	@Override
	public void hardDeleteNotes(Integer id) throws Exception {

		Notes notes = notesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes Not Found"));

		if (notes.getIsDeleted()) {
			notesRepository.delete(notes);
		} else {
			throw new IllegalArgumentException("Sorry You Can't hard Delete Direclty");
		}

	}

	@Override
	public void emptyRecycleBin(Integer userId) {
		
		List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
		
		if(!CollectionUtils.isEmpty(recycleNotes)) {
			
			notesRepository.deleteAll(recycleNotes);
		}
		
	}

	@Override
	public void favouriteNotes(Integer noteId) throws Exception {

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
	public void unFavouriteNotes(Integer FavouriteNoteId) throws Exception {
		

		 FavouriteNotes favNote = favouriteNotesRepository.findById(FavouriteNoteId)
				.orElseThrow(() -> new ResourceNotFoundException("Favourite Note Not Found"));
		
		favouriteNotesRepository.delete(favNote);
		
	}

	@Override
	public List<FavouriteNotesDto> getUserFavouriteNotes() {
		
		Integer userId = 1;
		List<FavouriteNotes> favNotes = favouriteNotesRepository.findByUserId(userId);

		return favNotes.stream().map((FavouriteNotes favN) -> modelMapper.map(favN, FavouriteNotesDto.class))
				.toList();

	}

}
