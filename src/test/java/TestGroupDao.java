import model.Group;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import persestince.ConnectionManager;
import persestince.GroupDAO;

import java.util.List;

public class TestGroupDao {
    private GroupDAO groupDAO;

    @Before
    public void init(){
        groupDAO = new GroupDAO();
    }

    @Test
    public void testGetAllGroups() {

        String expected = "[Group(id=1, name=group1), Group(id=2, name=group2), Group(id=3, name=group3)]";
        List<Group> students = groupDAO.getAll();
        System.out.println(students);

        Assert.assertEquals(expected, students.toString());
    }

    @After
    public void closeConnection() {
        ConnectionManager.getConnectionManager().closeConnection();
    }

    @Test
    public void getGroupByID() {

        String expected = "Group(id=1, name=group1)";
        Group group = groupDAO.getOneByID(1);

        Assert.assertEquals(expected, group.toString());

    }

    @Test
    public void getGroupByName() {

        String expected = "Group(id=2, name=group2)";
        Group group = groupDAO.getOneByName("group2");

        Assert.assertEquals(expected, group.toString());

    }

    @Test
    public void addGroup() {

        Assert.assertTrue(groupDAO.addNewEntity(new Group(-1, "newGroup2")));

    }

    @Test
    public void updateGroup() {

        Assert.assertTrue(groupDAO.updateEntityInfo(new Group(4, "newGroupUpdate")));

    }

    @Test
    public void selectGroupsbyLesson() {

        String lesson = "java";
        List<Group> groups = groupDAO.getGroupsByLesson(lesson);
        String expected = "[Group(id=1, name=group1), Group(id=3, name=group3)]";

        Assert.assertEquals(expected, groups.toString());

    }

    @Test
    public void selectGroupsWithAllLessons() {

        List<Group> groups = groupDAO.getGroupsWhoLearnAllLessons();

        String expected = "[Group(id=1, name=group1), Group(id=3, name=group3)]";

        Assert.assertEquals(expected, groups.toString());

    }

    @Test
    public void selectGroupsWithMoreThan3StudentsLearnLesson() {

        String lesson = "philosophy";
        List<Group> groups = groupDAO.getGroupsWithMoreThan3StudentsLearnLesson(lesson);

        String expected = "[Group(id=1, name=Vasa), Group(id=3, name=Vana)]";

        Assert.assertEquals(expected, groups.toString());

    }
}
