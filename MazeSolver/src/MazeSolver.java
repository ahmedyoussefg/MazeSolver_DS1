import javax.lang.model.type.NullType;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

import static java.lang.System.exit;

public class MazeSolver implements IMazeSolver {
    public static class mazePoint {
        int x;
        int y;
        mazePoint parent;
    }
    public static void main(String[] args){
        File maze = new File("maze.txt");
        MazeSolver solver = new MazeSolver();
        try {
            char[][] maze_grid = solver.mazeReader(maze);
        } catch (FileNotFoundException e) {
            System.out.println("File not found in the project folder!");
            throw new RuntimeException(e);
        }
        int option = -1;
        Scanner in = new Scanner(System.in);
        while (option == -1)
        {
            System.out.println("Solve using DFS or BFS or both. (Choose 1/2/3 for DFS/BFS/both respectively)");
            option = in.nextInt();
            switch (option)
            {
                case 1:
                    int[][] bfs_grid;
                    bfs_grid = solver.solveBFS(maze);
                    solver.printPath(bfs_grid, 1); // 1 for bfs
                    break;
                case 2:
                    int[][] dfs_grid;
                    dfs_grid = solver.solveDFS(maze);
                    solver.printPath(dfs_grid, 2); // 2 for dfs
                    break;
                case 3:
                    int[][] bfs2_grid;
                    bfs2_grid = solver.solveBFS(maze);
                    solver.printPath(bfs2_grid, 1); // 1 for bfs
                    int[][] dfs2_grid;
                    dfs2_grid = solver.solveDFS(maze);
                    solver.printPath(dfs2_grid, 2); // 2 for dfs
                    break;
                default:
                    System.out.println("Choose from only 1,2,3");
                    break;
            }
        }
    }
    // function to print bfs/dfs path, if algo = 1 then bfs if algo = 2 then dfs
    public void printPath(int[][] path, int algo)
    {
        int non_zeros = 0;
        for (int i = 0; i < path.length; i++)
        {
            for (int j = 0; j < path[0].length; j++)
            {
                if (path[i][j] != 0)
                    non_zeros++;
            }
        }
        mazePoint[] print_list = new mazePoint[non_zeros];
        int indx = 0;
        int search_for = non_zeros;
        while (search_for != 0)
        {
            boolean found = false;
            for (int i = 0; i < path.length; i++)
            {
                for (int j = 0; j < path[0].length; j++)
                {
                    if (path[i][j] == search_for)
                    {
                        found = true;
                        mazePoint point = new mazePoint();
                        point.x = i;
                        point.y = j;
                        print_list[indx] = point;
                        indx++;
                        search_for--;
                        break;
                    }
                }
                if (found)
                    break;
            }
        }
        switch (algo) {
            case 1:
                System.out.print("BFS: ");
                for (int i = 0; i < non_zeros; i++) {
                    System.out.print("(" + print_list[i].x + ", " + print_list[i].y + ")");
                    if (i != non_zeros - 1)
                        System.out.print(" -> ");
                }
                System.out.println();
                break;
            case 2:
                System.out.print("DFS: ");
                for (int i = non_zeros - 1; i >= 0; i--) {
                    System.out.print("(" + print_list[i].x + ", " + print_list[i].y + ")");
                    if (i != 0)
                        System.out.print(" -> ");
                }
                System.out.println();
                break;
        }
    }
    // function to get the start point index
    public mazePoint getStart(char[][] grid ,int rows, int cols)
    {
        mazePoint start = new mazePoint();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                if (grid[i][j] == 'S')
                {
                    start.x = i;
                    start.y = j;
                    start.parent = null;
                    return start;
                }
            }
        }
        return null;
    }
    public char[][] mazeReader(File maze) throws FileNotFoundException {
        // function to read maze file then converts it to 2d - char - array.
        Scanner read_maze = new Scanner(maze);
        String dims_str = read_maze.nextLine();
        String[] dims_arr = dims_str.split(" ");
        int[] dims = new int[]{Integer.parseInt(dims_arr[0]),Integer.parseInt((dims_arr[1]))};
        String[] maze_str = new String[dims[0]];
        int c = 0;
        while (read_maze.hasNext())
        {
            maze_str[c++] = read_maze.nextLine();
        }
        char[][] maze_grid = new char[dims[0]][dims[1]];
        for (int i = 0; i < dims[0]; i++)
        {
            maze_grid[i] = maze_str[i].toCharArray();
        }
        int end_point_counter  = 0;
        int start_point_counter = 0;
        for (int i = 0; i < dims[0]; i ++)
        {
            for (int j = 0; j < dims[1]; j++)
            {
                if (maze_grid[i][j] == 'S')
                    start_point_counter++;
                if (maze_grid[i][j] == 'E')
                    end_point_counter++;
            }
        }
        if (start_point_counter >=2)
        {
            System.out.println("Error! more than one start point");
            exit(1);
        }
        if (start_point_counter == 0)
        {
            System.out.println("Error! NO Start point found!");
            exit(1);
        }
        if (end_point_counter >=2)
        {
            System.out.println("Error! more than one end point");
            exit(1);
        }
        if (end_point_counter == 0)
        {
            System.out.println("Error! NO End point found!");
            exit(1);
        }
        return maze_grid;
    }
    /**
     * Read the maze file, and solve it using Breadth First Search
     *
     * @param maze maze file
     * @return the coordinates of the found path from point ’S’
     * to point ’E’ inclusive, or null if no path is found.
     */
    public int[][] solveBFS(File maze) {
        MazeSolver solver = new MazeSolver();
        char[][] maze_grid;
        try {
            maze_grid = solver.mazeReader(maze);
        } catch (FileNotFoundException e) {
            System.out.println("Maze File not found");
            throw new RuntimeException(e);
        }
        int rows = maze_grid.length;
        int cols = maze_grid[0].length;
        mazePoint start = getStart(maze_grid, rows, cols);
        LinkedListQueue.SingleLinkedList.Node start_node = new LinkedListQueue.SingleLinkedList.Node();
        start_node.data = start;
        if (start == null)
        {
            System.out.println("No start point!");
            exit(1);
        }
        boolean found_end = false;
        boolean[][] visited = new boolean[rows][cols]; // set to false as default value
        visited[start.x][start.y] = true;
        LinkedListQueue q = new LinkedListQueue();
        q.enqueue(start_node);
        mazePoint[] arr = new mazePoint[rows*cols];
        mazePoint end_point = new mazePoint();

        int count = 0;
        int[] dc = {0,0,-1,1}; // direction column vector
        int[] dr = {-1,1,0,0}; // direction row vector
        while (!q.isEmpty())
        {
            mazePoint element = new mazePoint();
            LinkedListQueue.SingleLinkedList.Node element_node = (LinkedListQueue.SingleLinkedList.Node) q.front;

            element = element_node.data;
            int curr_row = element.x;
            int curr_col = element.y;
            for (int i = 0; i < 4; i++)
            {
                int adj_row = curr_row + dr[i];
                int adj_col = curr_col + dc[i];
                if (adj_row < 0 || adj_row >= rows)
                    continue;
                if (adj_col < 0 || adj_col >= cols)
                    continue;
                if (maze_grid[adj_row][adj_col] == '#')
                    continue;
                if (visited[adj_row][adj_col] == true)
                    continue;
                visited[adj_row][adj_col] = true;
                mazePoint neighbour =  new mazePoint();
                neighbour.x = adj_row;
                neighbour.y = adj_col;
                neighbour.parent = element;
                LinkedListQueue.SingleLinkedList.Node neighbour_node = new LinkedListQueue.SingleLinkedList.Node();
                neighbour_node.data = neighbour;
                q.enqueue(neighbour_node);
            }

            q.dequeue();
            arr[count++] = element;
            if (maze_grid[curr_row][curr_col] == 'E')
            {
                end_point = element;
                found_end = true;
                break;
            }
        }
        if (found_end == false)
        {
            System.out.println("BFS: Can't find a path to the end.");
            exit(1);
        }
        int[][] path_map = new int[rows][cols];
        int move_count = 1;
        int end_index = 0;
        for (int i = count - 1; i >= 0; i--)
        {
            if (arr[i].equals(end_point)) {
                end_index = i;
                break;
            }
        }
        mazePoint temp = arr[end_index];
        while (temp != null)
        {
            path_map[temp.x][temp.y] = move_count;
            move_count++;
            temp = temp.parent;
        }
        return path_map;
    }
    boolean isAvaliable(char[][] maze, int i, int j, boolean [][] visited)
    {
        return !(i < 0 || j < 0 || i>= maze.length || j >= maze[0].length || visited[i][j] == true || maze[i][j] == '#');
    }
    /**
     * Read the maze file, and solve it using Depth First Search
     *
     * @param maze maze file
     * @return the coordinates of the found path from point ’S’
     * to point ’E’ inclusive, or null if no path is found.
     */
    public int[][] solveDFS(File maze) {
        MazeSolver solver = new MazeSolver();
        char[][] maze_grid;
        try {
            maze_grid = solver.mazeReader(maze);
        } catch (FileNotFoundException e) {
            System.out.println("Maze File not found");
            throw new RuntimeException(e);
        }
        int rows = maze_grid.length;
        int cols = maze_grid[0].length;
        mazePoint start = getStart(maze_grid, rows, cols);
        if (start == null)
        {
            System.out.println("Start point is not found.");
            exit(1);
        }
        boolean[][] visited = new boolean[rows][cols];
        visited[start.x][start.y] = true;
        MyStack s = new MyStack();
        MyStack.SingleLinkedList.Node start_node = new MyStack.SingleLinkedList.Node();
        start_node.value = start;
        s.push(start_node);
        boolean found_end = false;
        int curr_row = start.x;
        int curr_col = start.y;
        // Assuming the priority of the dfs algorithm direction is 1.north, 2. south, 3. west, 4.east
        while (!s.isEmpty() || !found_end)
        {
            if(isAvaliable(maze_grid,curr_row - 1,curr_col,visited))
            {
                curr_row = curr_row - 1;
                MyStack.SingleLinkedList.Node temp_n = new MyStack.SingleLinkedList.Node();
                mazePoint temp = new mazePoint();
                temp.x = curr_row;
                temp.y = curr_col;
                temp_n.value = temp;
                s.push(temp_n);
                visited[curr_row][curr_col] = true;
                if (maze_grid[curr_row][curr_col] == 'E')
                {
                    found_end = true;
                    break;
                }
            }
            else if(isAvaliable(maze_grid,curr_row + 1,curr_col,visited))
            {
                curr_row++;
                MyStack.SingleLinkedList.Node temp_n = new MyStack.SingleLinkedList.Node();
                mazePoint temp = new mazePoint();
                temp.x = curr_row;
                temp.y = curr_col;
                temp_n.value = temp;
                s.push(temp_n);
                visited[curr_row][curr_col] = true;
                if (maze_grid[curr_row][curr_col] == 'E')
                {
                    found_end = true;
                    break;
                }
            }
            else if(isAvaliable(maze_grid,curr_row,curr_col-1,visited))
            {
                curr_col--;
                MyStack.SingleLinkedList.Node temp_n = new MyStack.SingleLinkedList.Node();
                mazePoint temp = new mazePoint();
                temp.x = curr_row;
                temp.y = curr_col;
                temp_n.value = temp;
                s.push(temp_n);
                visited[curr_row][curr_col] = true;
                if (maze_grid[curr_row][curr_col] == 'E')
                {
                    found_end = true;
                    break;
                }
            }
            else if(isAvaliable(maze_grid,curr_row,curr_col + 1,visited))
            {
                curr_col++;
                MyStack.SingleLinkedList.Node temp_n = new MyStack.SingleLinkedList.Node();
                mazePoint temp = new mazePoint();
                temp.x = curr_row;
                temp.y = curr_col;
                temp_n.value = temp;
                s.push(temp_n);
                visited[curr_row][curr_col] = true;
                if (maze_grid[curr_row][curr_col] == 'E')
                {
                    found_end = true;
                    break;
                }
            }
            else
            {
                s.pop();
                MyStack.SingleLinkedList.Node pk = (MyStack.SingleLinkedList.Node) s.peek();
                curr_row = pk.value.x;
                curr_col = pk.value.y;
            }
        }
        if (!found_end)
        {
            System.out.println("There is no path to reach end point.");
            exit(1);
        }
        int[][] map = new int[rows][cols];
        int stack_size = s.size();
        int move_count = stack_size;
        for (int i = 0; i < stack_size; i++)
        {
            MyStack.SingleLinkedList.Node temp = (MyStack.SingleLinkedList.Node) s.peek();
            s.pop();
            map[temp.value.x][temp.value.y] = move_count;
            move_count--;
        }
        return map;
    }
}
