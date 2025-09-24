package item;

public class EventItem {
    private int id;
    private String title;
    private String dateIso; // yyyy-MM-dd
    private String colorHex; // #RRGGBB

    public EventItem() { }

    public EventItem(int id, String title, String dateIso, String colorHex) {
        this.id = id;
        this.title = title;
        this.dateIso = dateIso;
        this.colorHex = colorHex;
    }

    public EventItem(String title, String dateIso, String colorHex) {
        this.title = title;
        this.dateIso = dateIso;
        this.colorHex = colorHex;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDateIso() { return dateIso; }
    public void setDateIso(String dateIso) { this.dateIso = dateIso; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }
}
