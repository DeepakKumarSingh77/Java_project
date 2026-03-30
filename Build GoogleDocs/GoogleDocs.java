import java.util.List;
import java.util.ArrayList;

abstract class DocumentElement{
    public abstract void render();
}

class TextElement extends DocumentElement{
    String name;
    public TextElement(String name){
        this.name = name;
    }
    @Override
    public void render() {
          System.out.println("Rendering Text Element: " + name);
    }
}

class ImageElement extends DocumentElement{
    String name;
    public ImageElement(String name){
        this.name = name;
    }
    @Override
    public void render() {
          System.out.println("Rendering Image Element: " + name);
    }
}


class Document{
    List<DocumentElement> elements=new ArrayList<>();
    public void addElement(DocumentElement element){
        elements.add(element);
    }
    public void render(){
        for(DocumentElement element: elements){
            element.render();
        }
    }
}

abstract class Persistance_DB{
    public abstract void save(Document document);
}

class SQL_DB extends Persistance_DB{
    @Override
    public void save(Document document) {
        System.out.println("Saving document to SQL Database");
    }
}

class MongoDB extends Persistance_DB{
    @Override
    public void save(Document document) {
        System.out.println("Saving document to MongoDB");
    }
}

class Document_Render{
    public void render(Document document){
        document.render();
    }
}

class DocumentEditor{
    Document document;
    Persistance_DB db;
    public DocumentEditor(Document document, Persistance_DB db){
        this.document = document;
        this.db = db;
    }
    public void addText(String name){
        document.addElement(new TextElement(name));
    }
    public void addImage(String name){
        document.addElement(new ImageElement(name));
    }
    public void save(){
        db.save(document);
    }
}
public class GoogleDocs{
    public static void main(String[] args) {
        Document document = new Document();
        Persistance_DB db = new SQL_DB();
        DocumentEditor editor = new DocumentEditor(document, db);
        editor.addText("Hello World");
        editor.addImage("Image1.png");
        editor.save();
            Document_Render renderer = new Document_Render();
            renderer.render(document);
    }
}