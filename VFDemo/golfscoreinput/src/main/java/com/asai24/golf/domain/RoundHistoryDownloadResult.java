package com.asai24.golf.domain;

import com.asai24.golf.Constant.ErrorServer;
import com.asai24.golf.adapter.AdapterFragmentHistory;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Akira Sosa
 * entity represents the response for /v1/histories/list_history.json
 */
@SuppressWarnings("serial")
public class RoundHistoryDownloadResult implements Serializable {
	
	private int total = 1;
	private ArrayList<HistoryObj> histories = new ArrayList<>();
	private int page = 1;
	private ErrorServer errorStatus;

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public ArrayList<HistoryObj> getHistories() {
		return histories;
	}
	public void setHistories(ArrayList<HistoryObj> histories) {
		this.histories = histories;
	}
	public ErrorServer getErrorStatus() {
		return errorStatus;
	}
	public void setErrorStatus(ErrorServer errorStatus) {
		this.errorStatus = errorStatus;
	}
	
	public boolean hasMoreResult() {
		return total > page;
	}
	public void addItemIntoList(HistoryObj obHistory){
		this.histories.add(obHistory);
	}
	public void addListIntoList(RoundHistoryDownloadResult roundHistoryDownloadResult){
		for (HistoryObj ob : roundHistoryDownloadResult.getHistories()){
			ob.setViewType(AdapterFragmentHistory.TYPE_HISTORY.ITEMS);
			this.histories.add(ob);
		}
		setPage(roundHistoryDownloadResult.getPage());
		setTotal(roundHistoryDownloadResult.getTotal());
	}

}
