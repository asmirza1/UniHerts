package asmirza.uniherts;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ASMIRZA on 13/02/2015.
 */
public class MapXML {

    private static MapXML instanceMapXML;
    private HashMap<String, Building> buildings = new HashMap<>();
    private HashMap<String, Parking> parkings = new HashMap<>();

    private ArrayList<Building> buildingPlaces;
    private ArrayList<Room> classRoomPlaces;
    private ArrayList<Room> toiletPlaces;
    private ArrayList<Room> accessibleToiletPlaces;
    private ArrayList<Room> shopsPlaces;
    private ArrayList<Room> learningZonePlaces;
    private ArrayList<Room> foodPlaces;
    private ArrayList<Room> doorsPlaces;
    private ArrayList<Room> liftPlaces;
    private ArrayList<Room> showersChangingPlaces;

    public MapXML() {

        buildingPlaces = new ArrayList<Building>();
        classRoomPlaces = new ArrayList<Room>();
        toiletPlaces = new ArrayList<Room>();
        accessibleToiletPlaces = new ArrayList<Room>();
        shopsPlaces = new ArrayList<Room>();
        learningZonePlaces = new ArrayList<Room>();
        foodPlaces = new ArrayList<Room>();
        doorsPlaces = new ArrayList<Room>();
        liftPlaces = new ArrayList<Room>();
        showersChangingPlaces = new ArrayList<Room>();
    }

    public static MapXML getInstance() {
        if (instanceMapXML == null) {
            instanceMapXML = new MapXML();
        }
        return instanceMapXML;
    }


    public void readXMLBuildingMarkers(XmlPullParser xpp) {

        Building building;
        Room room;


        String name = "";
        String address = "";
        Double lat = 0.0;
        Double lang = 0.0;
        float zoom = 0;


        String roomType = "";
        String roomInfo = "";

        building = new Building(lat, lang, name, address, zoom);

        int event;
        String text = null;
        try {
            event = xpp.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String tageName = xpp.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (tageName.equals("rooms")) {

                            boolean stopLoop = false;


                            int event2 = xpp.getEventType();
                            String tageName2 = "";
                            while (!stopLoop) {

                                tageName2 = xpp.getName();
                                switch (event2) {
                                    case XmlPullParser.START_TAG:
                                        break;
                                    case XmlPullParser.TEXT:
                                        text = xpp.getText();
                                        break;
                                    case XmlPullParser.END_TAG:
                                        if (tageName2.equals("rooms")) {
                                            stopLoop = true;
                                        }
                                        if (tageName2.equals("name")) {
                                            name = text;
                                        } else if (tageName2.equals("lat")) {
                                            lat = Double.parseDouble(text);
                                        } else if (tageName2.equals("lang")) {
                                            lang = Double.parseDouble(text);
                                        } else if (tageName2.equals("zoom")) {
                                            zoom = Float.valueOf(text);
                                        } else if (tageName2.equals("type")) {
                                            roomType = text;
                                        } else if (tageName2.equals("info")) {
                                            roomInfo = text;
                                        } else if (tageName2.equals("room")) {
                                            System.out.println("" + tageName);
                                            System.out.println("ROOM END ");
                                            room = new Room(lat, lang, name, zoom, roomInfo, building.getName(), roomType);
                                            System.out.println("ROOM READY " + room.getName());
                                            building.addRoom(room);
                                            saveRoomsByType(room);
                                            System.out.println("ROOM added ");
                                        } else {
                                        }
                                        break;
                                }
                                if (!stopLoop) {
                                    event2 = xpp.next();
                                }
                            }
                        }
                    case XmlPullParser.TEXT:
                        text = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tageName.equals("building")) {
                            System.out.println("" + tageName);
                            System.out.println("BUILDING ADDED");
                            buildings.put(building.getName(), building);
                            buildingPlaces.add(building);
                        }
                        if (tageName.equals("name")) {
                            name = text;
                        } else if (tageName.equals("address")) {
                            address = text;
                        } else if (tageName.equals("lat")) {
                            lat = Double.parseDouble(text);
                        } else if (tageName.equals("lang")) {
                            lang = Double.parseDouble(text);
                        } else if (tageName.equals("zoom")) {
                            zoom = Float.valueOf(text);
                            building = new Building(lat, lang, name, address, zoom);
                            System.out.println("BUILDING READY " + building.getName());
                        } else {
                        }
                        break;
                }
                event = xpp.next();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void saveRoomsByType(Room room) {
        String roomsMarkersType = room.getType();
        if (roomsMarkersType.equalsIgnoreCase("classroom")) {
            classRoomPlaces.add(room);
        } else if (roomsMarkersType.equalsIgnoreCase("toilet")) {
            toiletPlaces.add(room);
        } else if (roomsMarkersType.equalsIgnoreCase("accessiblity_toilet")) {
            accessibleToiletPlaces.add(room);
        } else if (roomsMarkersType.equalsIgnoreCase("food_outlet")) {
            foodPlaces.add(room);
        } else if (roomsMarkersType.equalsIgnoreCase("shop")) {
            shopsPlaces.add(room);
        }
    }

    public void readXMLParkingMarkers(XmlPullParser xpp) {

        Log.i("readXMLParkingMarkers", "METHOD");
        String name = "";
        Double lat = 0.0;
        Double lang = 0.0;
        float zoom = 0;
        String address = "";
        String spaceDetails = "";
        String code = "";
        int type = 0;
        String features = "";
        Boolean isDisable = false;
        String restrictionsTime = "";
        String parkingRestrictions = "";
        Parking parking = new Parking();

        int event;
        String text = null;
        try {
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("parking")) {
                            // create a new instance of parking
                            parking = new Parking();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("parking")) {
                            // add employee object to list
                            parking = new Parking(lat, lang, name, zoom, address, code, type, isDisable, spaceDetails, features, parkingRestrictions, restrictionsTime);
                            Log.i("PARKING CREATED", parking.toString());
                            parkings.put(parking.getName(), parking);
                        } else if (tagname.equalsIgnoreCase("name")) {
                            name = text;
                        } else if (tagname.equalsIgnoreCase("lat")) {
                            lat = Double.parseDouble(text);
                        } else if (tagname.equalsIgnoreCase("lang")) {
                            lang = Double.parseDouble(text);
                        } else if (tagname.equalsIgnoreCase("address")) {
                            address = text;
                        } else if (tagname.equalsIgnoreCase("zoom")) {
                            zoom = Float.valueOf(text);
                        } else if (tagname.equalsIgnoreCase("code")) {
                            code = text;
                        } else if (tagname.equalsIgnoreCase("type")) {
                            Log.i("type", text);
                            type = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("isDisable")) {
                            isDisable = Boolean.parseBoolean(text);
                        } else if (tagname.equalsIgnoreCase("spaceDetails")) {
                            spaceDetails = text;
                        } else if (tagname.equalsIgnoreCase("features")) {
                            features = text;
                        } else if (tagname.equalsIgnoreCase("restrictionsTime")) {
                            restrictionsTime = text;
                        } else if (tagname.equalsIgnoreCase("parkingRestrictions")) {
                            parkingRestrictions = text;
                        }
                        break;

                    default:
                        break;
                }
                eventType = xpp.next();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public HashMap<String, Building> getBuildings(XmlPullParser xpp) {
        try {
            Thread.sleep(250);
        } catch (InterruptedException ignore) {
        }
        if (buildings.size() == 0) {
            readXMLBuildingMarkers(xpp);
        }
        return buildings;
    }

    public HashMap<String, Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(HashMap<String, Building> buildings) {
        this.buildings = buildings;
    }

    public HashMap<String, Parking> getParkings(XmlPullParser xpp) {
        if (parkings.size() == 0) {
            readXMLParkingMarkers(xpp);
        }
        return parkings;
    }


    public HashMap<String, Parking> getParkings() {
        return parkings;
    }

    public ArrayList<Building> getBuildingPlaces() {
        return buildingPlaces;
    }

    public ArrayList<Room> getClassRoomPlaces() {
        return classRoomPlaces;
    }

    public ArrayList<Room> getToiletPlaces() {
        return toiletPlaces;
    }

    public ArrayList<Room> getAccessibleToiletPlaces() {
        return accessibleToiletPlaces;
    }

    public ArrayList<Room> getShopsPlaces() {
        return shopsPlaces;
    }

    public ArrayList<Room> getLearningZonePlaces() {
        return learningZonePlaces;
    }

    public ArrayList<Room> getFoodPlaces() {
        return foodPlaces;
    }

    public ArrayList<Room> getDoorsPlaces() {
        return doorsPlaces;
    }

    public ArrayList<Room> getLiftPlaces() {
        return liftPlaces;
    }

    public ArrayList<Room> getShowersChangingPlaces() {
        return showersChangingPlaces;
    }
}
