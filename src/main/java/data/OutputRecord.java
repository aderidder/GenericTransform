package data;

import identifiers.settings.Settings;
import shared.Shared;

import java.util.stream.Collectors;

/**
 * Created by sa.d.ridder on 1/16/2017.
 */
public class OutputRecord extends Record{
    public OutputRecord(int size, Bookkeeper bookkeeper) {
        super(size, bookkeeper);
    }

    public String toString(){
        Settings settings = Shared.getSettings();
        String dataString = record.stream().collect(Collectors.joining("\t"));
        dataString = settings.getStudyName()+"\t"+settings.getSiteName()+"\t"+
                settings.getVisitName()+"\t"+settings.getVisitNr()+"\t"+dataString;
        return dataString;
    }
}
