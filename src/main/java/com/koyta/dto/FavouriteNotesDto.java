package com.koyta.dto;

import com.koyta.entity.Notes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavouriteNotesDto {

	private Integer id;

	private Notes notes;

	private Integer userId;

}
