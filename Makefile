KC= kotlinc
SRC= AyudanteOrtografico.kt ConjuntoPalabras.kt PruebaOrtografia.kt TAD_PMLI.kt
OUT = main.jar

ALL: $(OUT)

$(OUT): 
	$(KC) $(SRC) -include-runtime -d $(OUT)