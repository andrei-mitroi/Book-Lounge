package com.licenta.bookLounge.service;

import com.licenta.bookLounge.model.BookResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedBookResponse {
	private List<BookResponse> content;
	private long totalElements;
}
