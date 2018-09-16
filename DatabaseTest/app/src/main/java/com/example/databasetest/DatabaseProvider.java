package com.example.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {

    //定义四个常量
    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;
    //定义全局Authority变量
    public static final String AUTHORITY = "com.example.databasetest.provider";
    //定义UriMatcher变量
    private static UriMatcher uriMatcher;
    //定义MyDatabaseHelper变量
    private MyDatabaseHelper dbHelper;

    //实例化UriMatcher 添加URL格式
    static {

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY , "book" , BOOK_DIR);
        uriMatcher.addURI(AUTHORITY , "book/#" , BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY , "category" , CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY , "category/#" , CATEGORY_ITEM);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //int
        int deletedRows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                //看着上面的参数写 不能马上理解的话
                deletedRows = db.delete("Book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                //uri.getPathSegments方法获取id
                String bookId = uri.getPathSegments().get(1);
                //对id进行约束
                deletedRows = db.delete("Book" , "id = ?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deletedRows = db.delete("Category",  selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deletedRows = db.delete("Category",  "id = ?", new String[]{categoryId});
                break;
            default:
                break;
        }
        return deletedRows;
    }

    //用于获取Uri对象所对应的MIME类型
    @Override
    public String getType(Uri uri) {

        /*
        一个内容URI所对应的MIME字符串主要由3部分组成
        必须以vnd开头
        如果内容URI以路径结尾 则后接android.cursor.dir/
        如果内容URI以id结尾 则后接android.cursor.item/
        最后接上vnd.<authority>.<path>

        例如路径结尾的 content://com.example.databasetest.provider/book
        则写成vnd.android.cursor.dir/vnd.com.example.databasetest.provider.book
        例如id结尾的 content://com.example.databasetest.provider/book/1
        则写成vnd.android.cursor.item/vnd.com.example.databasetest.provider.book
         */

        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.category";
        }
        return null;
    }

    //添加数据
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Uri
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                //第二个参数是给可为空的列自动赋值null
                long newBookId = db.insert("Book", null, values);
                //要求返回一个能代表这条数据的URI 所以要调用parse方法将内容URI解析成Uri对象 以新id结尾
                uriReturn = Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert("Category", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/category/" + newCategoryId);
                break;
                default:
                    break;
        }
        return uriReturn;
    }

    //实例化MyDatabaseHelper 返回true初始化ContentProvider
    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 2);
        return true;
    }

    //查询数据
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        //获取SQLiteDatabase实例 唯一的Read
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //通过传入uri判断用户想查询哪个表 再通过db.query进行查询 最后返回cursor
        Cursor cursor = null;
            switch (uriMatcher.match(uri)){
                case BOOK_DIR:
                    cursor = db.query("Book", projection, selection, selectionArgs, null ,null , sortOrder);
                    break;
                case  BOOK_ITEM:
                    //通过URI的getPathSegments方法将内容URI进行分割 放进字符串列表中 0位置表示路径 1位置标识id
                    String bookId = uri.getPathSegments().get(1);
                    //再对id进行约束 获取相应数据
                    cursor = db.query("Book", projection, "id = ?", new String[]{bookId}, null, null, sortOrder);
                    break;
                case CATEGORY_DIR:
                    cursor = db.query("Category", projection, selection, selectionArgs, null ,null , sortOrder);
                    break;
                case  CATEGORY_ITEM:
                    String categoryId = uri.getPathSegments().get(1);
                    cursor = db.query("Category", projection, "id = ?", new String[]{categoryId}, null, null, sortOrder);
                    break;
                    default:
                        break;
            }
        return cursor;
    }

    //更新数据
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //int
        int updatedRows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                //看着上面的参数写 不能马上理解的话
                updatedRows = db.update("Book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                //uri.getPathSegments方法获取id
                String bookId = uri.getPathSegments().get(1);
                //对id进行约束
                updatedRows = db.update("Book", values, "id = ?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updatedRows = db.update("Category", values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updatedRows = db.update("Category", values, "id = ?", new String[]{categoryId});
                break;
                default:
                    break;
        }
        return updatedRows;
    }
}
