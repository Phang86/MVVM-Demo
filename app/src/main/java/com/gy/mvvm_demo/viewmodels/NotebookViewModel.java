package com.gy.mvvm_demo.viewmodels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;

import com.gy.mvvm_demo.db.bean.Notebook;
import com.gy.mvvm_demo.repository.NotebookRepository;

import java.util.List;

public class NotebookViewModel extends BaseViewModel {

    private final NotebookRepository notebookRepository;

    public LiveData<List<Notebook>> notebooks;

    @ViewModelInject
    NotebookViewModel(NotebookRepository notebookRepository){
        this.notebookRepository = notebookRepository;
    }

    public void getNotebooks() {
        failed = notebookRepository.failed;
        notebooks = notebookRepository.getNotebooks();
    }

    /**
     * 删除笔记
     */
    public void deleteNotebook(Notebook... notebook) {
        notebookRepository.deleteNotebook(notebook);
        failed = notebookRepository.failed;
    }

    /**
     * 搜索笔记
     * @param input 输入内容
     */
    public void searchNotebook(String input) {
        notebooks = notebookRepository.searchNotebook(input);
        failed = notebookRepository.failed;
    }

}

