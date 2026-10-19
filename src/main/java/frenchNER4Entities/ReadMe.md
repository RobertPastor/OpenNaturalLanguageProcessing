## source

https://huggingface.co/datasets/CATIE-AQ/frenchNER_4entities/tree/main/data

## three java class to read the parquet files

## expected structure of the input parquet files

1) List<String> tokens,  
2) List<Integer> ner_tags,  
3) String dataset  


## blog

https://lbourdois.github.io/blog/NER/

CamemBERT architecture, grouped primarily under the NERmembert and Moderncamembert series.   
  
## 3 Entities versus 4 Entities
  
These models are structured into two categorization variants:  
• 3-Entity Models: Trained on a cleaned dataset named frenchNER_3entities containing over 420,000 rows.   
They identify Location (LOC), Person (PER), and Organization (ORG).

• 4-Entity Models: Trained on frenchNER_4entities with over 384,000 rows.  
They add a Miscellaneous (MISC) classification category.  