package frenchNER4Entities;

import java.util.List;

public class FrenchNER4EntitiesDataSchema {
	
	public static record FrenchNer4EntitiesDataRecord(

			List<String> tokens,
			List<Integer> ner_tags,
			String dataset
		) {

		public List<String> tokens() {
			return tokens;
		}
		public List<Integer> ner_tags() {
			return ner_tags;
		}
		public String dataset() {
			return dataset;
		}
	}
}
