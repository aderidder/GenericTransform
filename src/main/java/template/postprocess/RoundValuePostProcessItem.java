package template.postprocess;

import data.Record;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Level;

public class RoundValuePostProcessItem extends PostProcessItem {
	RoundValuePostProcessItem(String nrDigits, boolean checkPreprocessItemsFirst){
        super(checkPreprocessItemsFirst);
		this.nrDigits = nrDigits;
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
	}

	@Override public void performItemAction(Record outputRecord, int instanceNr, String uid){
		try {
			String curValue = outputRecord.getValue(uid);
			if (!curValue.equalsIgnoreCase("")) {
                BigDecimal d = new BigDecimal(curValue).setScale(Integer.parseInt(nrDigits), RoundingMode.HALF_UP).stripTrailingZeros();
				outputRecord.setValue(uid, d.toPlainString());
			}
		}  catch (Exception e){
			log.log(Level.SEVERE, "Problem performing RoundValue action: {0}", e.getMessage());
		}
	}

	@Override public boolean conditionMet(Record inputRecord, Record outputRecord, int instanceNr) {
		return true;
	}

    private final NumberFormat numberFormat = new DecimalFormat("0.00");
	private final String nrDigits;

}
