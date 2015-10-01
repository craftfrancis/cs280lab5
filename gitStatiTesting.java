/*Team 3 and 6
 *
 * Authors: Ayodele Hamilton, Elizabeth Persons, SJ Guillaumes,
 * Troy Dinga, Francis Craft, and Alex Means
 *
 *
*/

import static org.junit.Assert.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.*;
import org.junit.Test;

import java.util.*;
import java.lang.Object;
import java.util.Random;

public class gitStatiTesting
{
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(gitStatiTesting.class);
        for(Failure failure: result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

    @Test
    public void testOne() { //Test for the amount of files in a directory
		ArrayList<Node> children = new ArrayList<Node>();
        children.add("1");
        children.add("2");
        children.add("3");
        children.add("4");
        System.out.println("Checking the size of the list: ");
		gitStati stati = new gitStati();
		ArrayList<Node> repos = gitStati.stati(children);
        int expected = repos.size();
        for(int i = 0; i <= repos.size(); i++) {
            Random rand = new Random();
            ArrayList<Node> list = new ArrayList<Node>(children.size());
            for(int a = 0; a <= children.size(); a++) {
                list.add(a);
            }
            int result = repos / children.size();
            int actuals = result;
            assertEquals(expected, actuals);

        }
    }
}

/*    @Test
    public void testTwo() { //Test if the directory is a Node
        gitStati git = gitStati();

    }

} */
