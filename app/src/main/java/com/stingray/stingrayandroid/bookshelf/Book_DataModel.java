package com.stingray.stingrayandroid.bookshelf;

public class Book_DataModel {

	public int book_id;
	public String book_title;
	public String book_author;
	public String book_description;
	public String book_cover_image;
	public String book;
	public String book_language;
	public int status;
	public int deleted;
	public int created_by;
	public int created_dt;
	public int updated_dt;
	public int updated_by;

	public Book_DataModel(int book_id, String book_title, String book_author,
			String book_description, String book_cover_image, String book, String book_language, 
			int status, int deleted, int created_dt, int updated_dt, int updated_by) {
		this.book_id = book_id;
		this.book_title = book_title;
		this.book_author = book_author;
		this.book_description = book_description;
		this.book_cover_image = book_cover_image;
		this.book = book;
		this.book_language = book_language;
		this.status = status;
		this.deleted = deleted;
		this.created_dt = created_dt;
		this.updated_dt = updated_dt;
		this.updated_by = updated_by;
	}

	public int getBook_id() {
		return book_id;
	}

	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}

	public String getBook_title() {
		return book_title;
	}

	public void setBook_title(String book_title) {
		this.book_title = book_title;
	}

	public String getBook_author() {
		return book_author;
	}

	public void setBook_author(String book_author) {
		this.book_author = book_author;
	}

	public String getBook_description() {
		return book_description;
	}

	public void setBook_description(String book_description) {
		this.book_description = book_description;
	}

	public String getBook_cover_image() {
		return book_cover_image;
	}

	public void setBook_cover_image(String book_cover_image) {
		this.book_cover_image = book_cover_image;
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public String getBook_language() {
		return book_language;
	}

	public void setBook_language(String book_language) {
		this.book_language = book_language;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	public int getCreated_dt() {
		return created_dt;
	}

	public void setCreated_dt(int created_dt) {
		this.created_dt = created_dt;
	}

	public int getUpdated_dt() {
		return updated_dt;
	}

	public void setUpdated_dt(int updated_dt) {
		this.updated_dt = updated_dt;
	}

	public int getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(int updated_by) {
		this.updated_by = updated_by;
	}
}
