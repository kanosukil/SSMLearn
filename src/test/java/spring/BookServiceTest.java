package spring;

import com.bookstore.spring.entity.Book;
import com.bookstore.spring.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author VHBin
 * @date 2021/12/17 - 16:06
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class BookServiceTest {
    static ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

    @Test
    public void selectAll() {
        BookService service = (BookService) context.getBean("bookService");
        List<Book> list = service.selectAll();
        for (Book b : list)
            System.out.println(b.toString());
    }

}
