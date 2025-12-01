//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import javax.swing.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
// @author Moss LaFountain

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        boolean done = false;
        boolean needsToBeSaved = false;
        ArrayList<String> myArrList = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        String userInput;
        System.out.println("Menu:");
        System.out.println("---------------------------------");
        System.out.println("A – Add an item to the list");
        System.out.println("D – Delete an item from the list");
        System.out.println("I – Insert an item into the list");
        System.out.println("M - Move an item");
        System.out.println("O - Open file from disk");
        System.out.println("S - Save current file to disk");
        System.out.println("C - Clear the list");
        System.out.println("V – View the list");
        System.out.println("Q - Quit");
        while (!done) {
            System.out.println("---------------------------------");
            System.out.println("Your current list:");
            listDisplay(myArrList);
            System.out.println("---------------------------------");
            userInput = SafeInput.getRegExString(in, "Choose an option from the menu", "[AaDdIiMmOoSsCcVvQq]");
            userInput = userInput.toUpperCase();
            switch (userInput) {
                case "A":
                    needsToBeSaved = addList(in, myArrList);
                    break;
                case "D":
                    needsToBeSaved = delList(in, myArrList);
                    break;
                case "I":
                    needsToBeSaved = insertList(in, myArrList);
                    break;
                case "M":
                    needsToBeSaved = moveItem(in, myArrList);
                    break;
                case "O":
                    openFile();
                    break;
                case "S":
                    saveFile(in,  myArrList);
                    needsToBeSaved = false;
                    break;
                case "C":
                    needsToBeSaved = clearList(in, myArrList);
                    break;
                case "V":
                    printDisplay(myArrList);
                    break;
                case "Q":
                    if (needsToBeSaved){
                        done = SafeInput.getYNConfirm(in, "Are you sure you want to quit without saving?");
                }else {
                        done = SafeInput.getYNConfirm(in, "Are you sure you want to quit?");
                        break;
                    }

            }
        }

    }

    private static boolean addList(Scanner in, ArrayList<String> myArrList) {

        String add = SafeInput.getNonZeroLenString(in, "Input String you would like to add");
        myArrList.add(add);
        boolean needsToBeSaved = true;
        return needsToBeSaved;

    }

    private static boolean delList(Scanner in, ArrayList<String> myArrList) {

        int size = myArrList.size() - 1;
        boolean needsToBeSaved = false;
        int toDel;
        if (myArrList.size() == 0) {
            System.out.println("Nothing to delete!");
        } else {
            toDel = SafeInput.getRangedInt(in, "Which item would you like to delete?", 0, size);
            myArrList.remove(toDel);
            needsToBeSaved = true;
        }
        return needsToBeSaved;
    }

    private static boolean insertList(Scanner in, ArrayList<String> myArrList) {

        int size = myArrList.size();
        int whereInsert;
        String toInsert;
        whereInsert = SafeInput.getRangedInt(in, "Where would you like to insert?", 0, size);
        toInsert = SafeInput.getNonZeroLenString(in, "Input string you would like to insert");
        myArrList.add(whereInsert, toInsert);
        boolean needsToBeSaved = true;
        return needsToBeSaved;
    }


    private static void listDisplay(ArrayList<String> myArrList) {

        for (int x = 0; x < myArrList.size(); x++) {
            System.out.println(x + ". " + myArrList.get(x));
        }

    }

    private static void printDisplay(ArrayList<String> myArrList) {

        for (int x = 0; x < myArrList.size(); x++) {
            System.out.println(myArrList.get(x));
        }

    }

    private static boolean moveItem(Scanner in, ArrayList<String> myArrList) {
        int size = myArrList.size() - 1;
        boolean needsToBeSaved = false;
        int whatMove;
        int whereMove;
        if (myArrList.size() == 0) {
            System.out.println("Nothing to move!");
        } else {
            whatMove = SafeInput.getRangedInt(in, "Which item would you like to move?", 0, size);
            whereMove = SafeInput.getRangedInt(in, "Where would you like to move the item?", 0, size);
            needsToBeSaved = true;
            myArrList.add(whereMove, myArrList.get(whatMove));
            myArrList.remove(whatMove);
        }
        return needsToBeSaved;
    }

    private static void openFile() {
        JFileChooser chooser = new JFileChooser();
        File selectedFile;
        String rec = "";


        try {
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();
                InputStream in =
                        new BufferedInputStream(Files.newInputStream(file, CREATE));
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(in));

                int line = 0;
                while (reader.ready()) {
                    rec = reader.readLine();
                }
                reader.close(); // must close the file to seal it and flush buffer


            } else  // User closed the chooser without selecting a file
            {
                System.out.println("No file selected! \nExiting. Run the program again and select a file.");
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found!");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private static void saveFile(Scanner in, ArrayList<String> myArrList) {
        File workingDirectory = new File(System.getProperty("user.dir"));
        String fileName = "";
        fileName = SafeInput.getNonZeroLenString(in, "What would you like to name your file?");
        Path file = Paths.get(workingDirectory.getPath() + "\\src\\" + fileName + ".txt");

        try
        {
            OutputStream out =
                    new BufferedOutputStream(Files.newOutputStream(file, CREATE));
            BufferedWriter writer =
                    new BufferedWriter(new OutputStreamWriter(out));


            for(String rec : myArrList)
            {
                writer.write(rec, 0, rec.length());
                writer.newLine();

            }
            writer.close();
            System.out.println("Data file written!");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private static boolean clearList(Scanner in, ArrayList<String> myArrList) {
        boolean needsToBeSaved = false;
        boolean sure = SafeInput.getYNConfirm(in, "Are you sure you want to clear the list?");
        if (sure) {
            myArrList.clear();
            needsToBeSaved = true;
        }
        return needsToBeSaved;
    }
}
