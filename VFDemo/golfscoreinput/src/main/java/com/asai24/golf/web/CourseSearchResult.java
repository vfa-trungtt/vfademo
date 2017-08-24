package com.asai24.golf.web;

import com.asai24.golf.Constant;
import com.asai24.golf.domain.ClubObj;
import com.asai24.golf.domain.Course;

import java.util.List;

public class CourseSearchResult {
	private List<Course> courses;
	// CongVC 2012-05-11 Add new
	private List<ClubObj> clubs;
	private int currentPage;
	private int total;

	public CourseSearchResult() {
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public boolean hasNextPage() {
		return (Constant.COURSE_SEARCH_PER_PAGE * currentPage < total);
	}

	public void setClubs(List<ClubObj> clubs) {
		this.clubs = clubs;
	}

	public List<ClubObj> getClubs() {
		return clubs;
	}
}
