package example;

import db.Entity;
import db.Trackable;

import java.util.Date;

public class Document extends Entity implements Trackable {
    public String content;
    private Date creationDate;
    private Date lastModificationDate;
    public static final int DOC_ENTITY_CODE = 15;
    public Document(String content) {
        this.content = content;
    }
    @Override
    public Entity copy() {
        Document doc = new Document(this.content);
        doc.id = this.id;
        doc.creationDate = this.creationDate;
        doc.lastModificationDate = this.lastModificationDate;
        return doc;
    }
    @Override
    public int getEntityCode(){
        return DOC_ENTITY_CODE;
    }
    @Override
    public void setCreationDate(Date date){
        this.creationDate = date;
    }
    @Override
    public Date getCreationDate(){
        return this.creationDate;
    }
    @Override
    public void setLastModificationDate(Date date){
        this.lastModificationDate = date;
    }
    @Override
    public Date getLastModificationDate(){
        return this.lastModificationDate;
    }
}
