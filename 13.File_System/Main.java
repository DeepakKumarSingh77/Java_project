import java.util.ArrayList;
import java.util.List;

interface FileSystemItem {
    void ls(int indent);

    void openAll(int indent);

    int getSize();

    FileSystemItem cd(String name);

    String getName();

    boolean isFolder();

    FileSystemItem getParent();

    void setParent(FileSystemItem parent);
}

class File implements FileSystemItem {
    private String name;
    private int size;
    private FileSystemItem parent;

    public File(String n, int s) {
        name = n;
        size = s;
    }

    @Override
    public FileSystemItem getParent() {
        return parent;
    }

    @Override
    public void setParent(FileSystemItem parent) {
        this.parent = parent;
    }

    @Override
    public void ls(int indent) {
        String indentSpaces = " ".repeat(indent);
        System.out.println(indentSpaces + name);
    }

    @Override
    public void openAll(int indent) {
        String indentSpaces = " ".repeat(indent);
        System.out.println(indentSpaces + name);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public FileSystemItem cd(String name) {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isFolder() {
        return false;
    }
}

class Folder implements FileSystemItem {
    private String name;
    private List<FileSystemItem> children;
    private FileSystemItem parent;

    public Folder(String n) {
        name = n;
        children = new ArrayList<>();
    }

    public void add(FileSystemItem item) {
        item.setParent(this);
        children.add(item);
    }

    @Override
    public FileSystemItem getParent() {
        return parent;
    }

    @Override
    public void setParent(FileSystemItem parent) {
        this.parent = parent;
    }

    @Override
    public void openAll(int indent) {
        String indentSpaces = " ".repeat(indent);
        System.out.println(indentSpaces + "+ " + name);
        for (FileSystemItem child : children) {
            child.openAll(indent + 4);
        }
    }

    @Override
    public void ls(int indent) {
        String indentSpaces = " ".repeat(indent);
        for (FileSystemItem child : children) {
            if (child.isFolder()) {
                System.out.println(indentSpaces + "+ " + child.getName());
            } else {
                System.out.println(indentSpaces + child.getName());
            }
        }
    }

    @Override
    public int getSize() {
        int total = 0;
        for (FileSystemItem child : children) {
            total += child.getSize();
        }
        return total;
    }

    @Override
    public FileSystemItem cd(String path) {
        String[] parts = path.split("/");
        FileSystemItem current = this;

        for (String part : parts) {
            if (part.equals("") || part.equals("."))
                continue;

            if (part.equals("..")) {
                if (current.getParent() != null) {
                    current = current.getParent();
                }
            } else {
                boolean found = false;

                if (!current.isFolder())
                    return null;

                for (FileSystemItem child : ((Folder) current).children) {
                    if (child.isFolder() && child.getName().equals(part)) {
                        current = child;
                        found = true;
                        break;
                    }
                }

                if (!found)
                    return null;
            }
        }

        return current;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isFolder() {
        return true;
    }

}

public class Main {
    public static void main(String[] args) {
        Folder root = new Folder("root");
        root.add(new File("file1.txt", 1));
        root.add(new File("file2.txt", 1));

        Folder docs = new Folder("docs");
        docs.add(new File("resume.pdf", 1));
        docs.add(new File("notes.txt", 1));
        root.add(docs);

        Folder images = new Folder("images");
        images.add(new File("photo.jpg", 1));
        docs.add(images);

        // root.ls(0);

        // docs.ls(0);

        // root.openAll(0);

        // FileSystemItem cwd = root.cd("docs");
        // if (cwd != null) {
        // cwd.ls(0);
        // } else {
        // System.out.println("\nCould not cd into docs\n");
        // }

        FileSystemItem cwd = root.cd("docs/images");
        // cwd.ls(0);

        cwd = cwd.cd("..");
        // cwd.ls(0);

        if (cwd != null) {
            cwd.ls(0);
        } else {
            System.out.println("\nCould not cd into docs\n");
        }

        // System.out.println(root.getSize());
    }
}