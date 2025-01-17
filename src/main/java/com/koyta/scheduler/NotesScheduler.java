package com.koyta.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.koyta.entity.Notes;
import com.koyta.repository.NotesRepository;

@Component
public class NotesScheduler {

	@Autowired
	private NotesRepository notesRepository;

	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteNotesScheduler() {

		LocalDateTime cutOffDate = LocalDateTime.now().minusDays(7);

		List<Notes> deleteNotes = notesRepository.findAllByIsDeletedAndDeletedOnBefore(true, cutOffDate);
		
		notesRepository.deleteAll(deleteNotes);


	}

}
