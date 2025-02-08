/*
      A self-imposed challenge to get familiar with Java and polymorhphism by building 
      a Library Management System in 3 hours
*/


import java.util.Scanner;
import java.util.ArrayList;

// INTERACTION
class User
{
  private String m_name;
  private ArrayList<LibraryItem> borrowed;

  User (String name) {
    m_name = name;
    borrowed = new ArrayList<>();
  }

  public void borrow_item (LibraryItem item) {
    borrowed.add(item);
  }

  public void return_item (LibraryItem item) {
    borrowed.remove(item);
  }

  public String get_name () { return m_name; }

  public void display_borrowed_items ()
  {
    for (int i = 0; i < borrowed.size(); i++) {
      System.out.println("Entry " + i+1);
      borrowed.get(i).displayInfo();
    }
  }
}

class Access
{
  private ArrayList<User> users;

  Access ()
  {
    users = new ArrayList<>();
  }

  public void add_user (User user) {
    users.add(user);
  }

  public User get_user (int index) {
    return users.get(index);
  }

  public int[] check (String name) {
    for (int i = 0; i < users.size(); i++)  {
      if (users.get(i).get_name().equals(name))
        return new int[] {i, 1};
    }
    return new int[] {0, 0};
  }
}

class Prompt
{
  public String request (String message)
  {
    Scanner scanner = new Scanner(System.in);
    System.out.print(message);
    String input = scanner.nextLine();

    return input;
  }
}

// LIBRARY STRUCTURE
interface Borrowable
{
  abstract void borrow();
  abstract void returnItem();
}

class LibraryItem implements Borrowable
{
  protected String m_title;
  protected int m_id;
  protected boolean m_available;

  LibraryItem (String title, int id, boolean available) {
    m_title = title;
    m_id = id;
    m_available = available;
  }
  
  public void borrow() { m_available = false; }
  public void returnItem() { m_available = true; }
  public String get_title () { return m_title; }
  
  public void displayInfo()
  {
    String status = "Unavailable";
    if (m_available)
      status = "Available";
    System.out.print("TITLE: " + m_title + " ID: " + m_id + " STATUS: " + status);
  }

  public boolean status ()
  {
    return m_available;
  }
    

  public void status_change (boolean status)
  {
    if (status)
      m_available = true;
    else
      m_available = false;
  }
}

class Book extends LibraryItem
{
  protected String m_author;

  Book (String title, int id, boolean available, String author) {
    super(title, id, available);
    m_author = author;
  }
  
  @Override
  public void displayInfo()
  {
    super.displayInfo();
    System.out.println (" AUTHOR: " + m_author);
  }
}

class Magazine extends LibraryItem
{
  protected String m_issueNumber;
  
  Magazine (String issueNumber, String title, int id, boolean available)
  {
    super(title, id, available);
    m_issueNumber = issueNumber;
  }
  @Override
  public void displayInfo()
  {
    super.displayInfo();
    System.out.println (" ISSUE: " + m_issueNumber);
  }
}

class DVD extends LibraryItem
{
  protected String m_duration;
  
  DVD (String duration, String title, int id, boolean available)
  {
    super(title, id, available);
    m_duration = duration;
  }
  
  @Override
  public void displayInfo()
  {
    super.displayInfo();
    System.out.println (" DURATION: " + m_duration);
  }
}

class Library
{
  //private ArrayList<LibraryItem> items = new ArrayList<>();
  private ArrayList<ArrayList<LibraryItem>> items = new ArrayList<>();
  Library ()
  {
    for (int i = 0; i < 3; i++) {
      items.add(new ArrayList<>());
    }
  }

  public void add_item (LibraryItem item)
  {
    if (item instanceof Book)
      items.get(0).add(item);
    else if (item instanceof DVD)
      items.get(1).add(item);
    else if (item instanceof Magazine)
      items.get(2).add(item);
  }

  public LibraryItem get_item (int[] indices)
  {
    return items.get(indices[0]).get(indices[1]);
  }

  public int[] search (String title)
  {
    for (int i = 0; i < items.size(); i++)
    {
      for (int ii = 0; ii < items.get(i).size(); ii++) {
        if (items.get(i).get(ii).get_title().equals(title)) {
          System.out.println("FOUND A MATCH");
          return new int[]{i, ii, 1};
        }
      }
    }
    
    System.out.println(title + " does not exist");
    return new int[]{0, 0, 0};
  }

  public void infos (int[] indices) {
    items.get(indices[0]).get(indices[1]).displayInfo();
  }
}

// MAIN TESTDRIVE
public class LibrarySystem
{
  public static void main (String[] args)
  {
    System.out.println("\t\t\n Library System \n\n\n");

    /*
    LibraryItem[] items = new LibraryItem[3];
    items[0] = new Book("SnowCrash", 001, true, "Neil Stephenson");
    items[1] = new DVD("3 hrs", "Intersteller", 002, false);
    items[2] = new Magazine("GameCon", "343", 003, true);
    */

    Access user_lists = new Access();
    user_lists.add_user(new User("mytx"));
    user_lists.add_user(new User("root"));

    Library library = new Library();
    library.add_item(new Book("Snow Crash", 001, true, "Neil Stephenson"));
    library.add_item(new DVD("3hrs", "Intersteller", 002, true));
    library.add_item(new Magazine("343", "GameCon", 003, false));
    
    Prompt prompt = new Prompt();
    
    String input = prompt.request("LOGIN: ");
    int[] user_info = new int[2];
    user_info = user_lists.check(input);
    if (user_info[1] == 1)
      System.out.println("Access Granted");
    else
      System.out.println("Access Denied");
    
    int[] item_info = new int[3];

    input = prompt.request("Borrow(1) / Return(2): ");

    if (input.equals("1")) {
      item_info = library.search(prompt.request("\n\t SEARCH: "));
      if (item_info[2] == 0)
        System.out.println("NOT FOUND");
      else {
        library.infos(item_info);

        if (prompt.request("Would you like to borrow? Y/n: ").equals("Y")) {
          if (library.get_item(item_info).status()) {
            user_lists.get_user(user_info[0]).borrow_item(library.get_item(item_info));
            System.out.println("Borrowed");
            library.get_item(item_info).status_change(false);
            //library.infos(item_info);
          }
          else {
            System.out.println("Unavailable");
          }
        }
      }
    }
    else {
      user_lists.get_user(user_info[0]).borrow_item(library.get_item(new int[]{2, 0, 1}));
      user_lists.get_user(user_info[0]).display_borrowed_items();
      input = prompt.request("Return Entry: ");
      user_lists.get_user(user_info[0]).return_item(library.get_item(new int[]{2, 0, 1}));
      library.get_item(new int[] {2, 0, 1}).status_change(true);
    }

    for (int i = 0; i < 3; i++) {
      library.infos(new int[] {i, 0, 1});
    }
  }
}
