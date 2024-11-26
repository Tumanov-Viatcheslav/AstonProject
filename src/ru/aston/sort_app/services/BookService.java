package ru.aston.sort_app.services;

import ru.aston.sort_app.core.Book;
import ru.aston.sort_app.core.UserInputChoice;
import ru.aston.sort_app.dao.FileDAO;
import ru.aston.sort_app.services.sorts.SortStrategy;
import ru.aston.sort_app.services.sorts.shell.BookShellSort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookService implements Generator<Book>, SearchStrategy<Book>, SortStrategy<Book> {
    private final FileDAO<Book> fileDao;
    private final BookShellSort bookShellSort;
    private static final int DEFAULT_BOOK_COUNT = 10;

    public BookService(FileDAO<Book> fileDao) {
        this.fileDao = fileDao;
        this.bookShellSort = new BookShellSort();
    }

    @Override
    public List<Book> generate(UserInputChoice generateType) {
        switch (generateType) {
            case ACTION_BOOK_FILE_GENERATED:
                return fileDao.get(DEFAULT_BOOK_COUNT);
            case ACTION_BOOK_RANDOM_GENERATED:
                return generateRandomBooks(DEFAULT_BOOK_COUNT);
            case ACTION_BOOK_MANUAL_GENERATED:
                // !!!Надо реализовать логику ручного ввода
                System.out.println("Функция ручного ввода пока не реализована.");
                break;
            default:
                System.out.println("Неподдерживаемый тип генерации: " + generateType);
                break;
        }
        return List.of();
    }

    @Override
    public List<Book> find(Book item) {
        // Реализуйте поиск книг по автору и названию
        return fileDao.get(DEFAULT_BOOK_COUNT).stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(item.getAuthor()) &&
                        book.getTitle().equalsIgnoreCase(item.getTitle()))
                .toList();
    }

    @Override
    public void sort(ArrayList<Book> array) {
        bookShellSort.sort(array);
    }

    private List<Book> generateRandomBooks(int count) {
        Random random = new Random();
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String author = "Автор " + random.nextInt(100);
            String title = "Книга " + random.nextInt(1000);
            int pageCount = 50 + random.nextInt(950);

            Book book = new Book.Builder()
                    .setAuthor(author)
                    .setTitle(title)
                    .setPageCount(pageCount)
                    .build();

            books.add(book);
        }

        return books;
    }
}
