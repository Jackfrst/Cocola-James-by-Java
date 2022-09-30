package james.cocola.app.LocationRecyclerView;

public class LocationItems {
    private String typeName , typeCode ;

    public LocationItems(String typeName, String typeCode) {
        this.typeName = typeName;
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }
}
