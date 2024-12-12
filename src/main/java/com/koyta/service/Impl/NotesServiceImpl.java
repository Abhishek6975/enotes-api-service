package com.koyta.service.Impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koyta.dto.NotesDto;
import com.koyta.dto.NotesDto.CategoryDto;
import com.koyta.dto.NotesDto.FilesDto;
import com.koyta.dto.NotesResponse;
import com.koyta.entity.FilesDetails;
import com.koyta.entity.Notes;
import com.koyta.exception.ResourceNotFoundException;
import com.koyta.repository.CategoryRepository;
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
	
	@Value("${file.upload.path}")
	private String uploadPath;

	@Override
	public Boolean saveNotes(String notes,  MultipartFile file) throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();

		NotesDto notesDto = objectMapper.readValue(notes, NotesDto.class);
		
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

		Page<Notes> pageNotes = notesRepository.findByCreatedBy(userId, pageable);

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

}
