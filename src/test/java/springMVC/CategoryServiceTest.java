package springMVC;

import com.bookstore.spring.entity.Category;
import com.bookstore.spring.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author VHBin
 * @date 2021/12/17 - 22:31
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:../../../main/resources/springmvc.xml"})
public class CategoryServiceTest {
    @Autowired
    private CategoryService service;
    @Test
    public void findAll(){
        List<Category> list = service.selectAll();
        for (Category c : list)
            System.out.println(c.toString());
    }
}
