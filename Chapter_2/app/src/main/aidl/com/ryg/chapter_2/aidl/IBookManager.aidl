// IBookManager.aidl
package com.ryg.chapter_2.aidl;

import com.ryg.chapter_2.aidl.Book;

interface IBookManager {

     List<Book> getBookList();
     void addBook(in Book book);

}
