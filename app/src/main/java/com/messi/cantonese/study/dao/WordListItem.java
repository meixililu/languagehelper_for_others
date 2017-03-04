package com.messi.cantonese.study.dao;

import java.io.Serializable;

public class WordListItem implements Serializable {

	private String class_id;

	private int course_num;

	private int course_id = 1;

	private String title;

	private String word_num;

	public int getCourse_id() {
		return course_id;
	}

	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}

	public String getClass_id() {
		return class_id;
	}

	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}

	public int getCourse_num() {
		return course_num;
	}

	public void setCourse_num(int course_num) {
		this.course_num = course_num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWord_num() {
		return word_num;
	}

	public void setWord_num(String word_num) {
		this.word_num = word_num;
	}

}