# File System LLD

## Description
This project is a Low-Level Design (LLD) implementation of a simple file system in Java. It demonstrates object-oriented programming principles by modeling files and folders as classes that implement a common interface. The system supports basic file system operations such as listing contents, navigating directories, and calculating sizes.

## Features
- **File System Item Interface**: Defines common operations for files and folders.
- **File Class**: Represents individual files with name and size.
- **Folder Class**: Represents directories that can contain files and subfolders.
- **Operations**:
  - `ls(int indent)`: Lists the contents of a folder with indentation.
  - `openAll(int indent)`: Recursively lists all contents with tree structure.
  - `getSize()`: Returns the size of a file or the total size of a folder and its contents.
  - `cd(String path)`: Changes directory to a specified path (supports relative paths like ".." and "/").
  - `getName()`: Returns the name of the item.
  - `isFolder()`: Checks if the item is a folder.

## Project Structure
- `Main.java`: Contains the `FileSystemItem` interface, `File` class, `Folder` class, and a `Main` class with a demonstration.

## How to Run
1. Ensure you have Java installed on your system.
2. Navigate to the project directory: `cd 13.File_System`
3. Compile the code: `javac Main.java`
4. Run the program: `java Main`

## Sample Output
```
resume.pdf
notes.txt
+ images
```

This output shows the contents of the "docs" folder after navigating through the file system structure.

## Usage Example
The `Main` class creates a sample file system structure:
- root/
  - file1.txt
  - file2.txt
  - docs/
    - resume.pdf
    - notes.txt
    - images/
      - photo.jpg

It then demonstrates changing directories and listing contents.