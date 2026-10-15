package frenchNER4Entities;

import tech.tablesaw.api.Table;
import tech.tablesaw.api.StringColumn;


public class FrenchNER4EntitiesDataTable extends Table {

	public Table frenchNerDataTable = null;
	
	FrenchNER4EntitiesDataTable() {
		super("NER French 4 Entities");
	}
	
	public void createEmptyNerDataTable( ) {
		this.frenchNerDataTable = Table.create("French NER Data",

			StringColumn.create("tokens"),
			StringColumn.create("ner_tags"),
			StringColumn.create("dataset")
			);
	} 
}
