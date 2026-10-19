## warnings 
## crash - found unexpected annotations

## case 1 
l' (article le élidé) found before a START or and END tag.

## case 2
java.io.IOException: Found unexpected annotation: Menace fantôme ###<END>### ", le
        at opennlp.tools.namefind.NameSample.parse(NameSample.java:328)

decision to suppress all " and \" in regular expression

## case 3
java.io.IOException: Found unexpected annotation: Menace fantôme ###<END>### , le

## case 4
replace <START:misc>' <END> -> with -> <START> <END>

##case 5 -> <START>, Fribourg Code
replace <START:person>, <END> -> with -> <START> <END>
replace <START:location>, <END> -> with -> <START> <END>
replace <START:organization>, <END> -> with -> <START> <END>

## case 6 -> <START< tag followed by Spaces and an <END> TAG -> hence an empty tagged pattern ?