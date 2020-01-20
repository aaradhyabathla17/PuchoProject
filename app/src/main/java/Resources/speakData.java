package Resources;

import com.puchoInc.diya.R;
import com.puchoInc.diya.TextSpeakData.SpeakData;

public class speakData {
    public static SpeakData[] getSpeakDatas(){
        return SNAPBACKS;
    }

    public static final SpeakData Data1 = new SpeakData(R.drawable.capture1,
             9377376);
    public static final SpeakData Data2 = new SpeakData(R.drawable.capture2,
             9377377);
    public static final SpeakData Data3 = new SpeakData( R.drawable.capture3,
             9377378);
    public static final SpeakData Data4 = new SpeakData( R.drawable.capture4,
           9377379);
    public static final SpeakData Data5 = new SpeakData( R.drawable.capture5,
           9377380);
    public static final SpeakData Data6 = new SpeakData( R.drawable.capture6,9377381);
    public static final SpeakData Data7 = new SpeakData( R.drawable.capture7,9377382);
    public static final SpeakData Data8 = new SpeakData( R.drawable.capture8,
             9377383);

    public static final SpeakData[] SNAPBACKS = {Data1, Data2, Data3, Data4, Data5, Data6,Data7,Data8};


}
