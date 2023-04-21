package com.gy.mvvm_demo.viewmodels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;

import com.gy.mvvm_demo.db.bean.Notebook;
import com.gy.mvvm_demo.repository.NotebookRepository;

public class EditViewModel extends BaseViewModel {

    private final NotebookRepository notebookRepository;
    public LiveData<Notebook> notebook;

    @ViewModelInject
    EditViewModel(NotebookRepository notebookRepository){
        this.notebookRepository = notebookRepository;
    }

    /**
     * 添加笔记
     */
    public void addNotebook(Notebook notebook) {
        failed = notebookRepository.failed;
        notebookRepository.saveNotebook(notebook);
    }

    /**
     * 根据id查询笔记
     */
    public void queryById(int uid) {
        failed = notebookRepository.failed;
        notebook = notebookRepository.getNotebookById(uid);
    }

    /**
     * 更新笔记
     */
    public void updateNotebook(Notebook notebook) {
        failed = notebookRepository.failed;
        notebookRepository.updateNoteBook(notebook);
    }

    /**
     * 删除笔记
     */
    public void deleteNotebook(Notebook... notebook) {
        notebookRepository.deleteNotebook(notebook);
        failed = notebookRepository.failed;
    }

}

