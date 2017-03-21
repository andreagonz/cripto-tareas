#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include "des.h"
#include <math.h>

//get bit by position
unsigned char getBit(unsigned char *elements, size_t idx){
    size_t i = idx / CHAR_BIT;
    size_t b = 1 << (CHAR_BIT - 1);
    b >>= (idx % CHAR_BIT);
    b &= elements[i];
    if(b == 0)
	return 0;
    return 1;
}

//set bit by postion
void setBit(unsigned char *elements, size_t idx, unsigned char bit){
    if(getBit(elements, idx) == bit)
	return;
    size_t i = idx / CHAR_BIT;
    size_t m = 1 << (CHAR_BIT - 1);
    m >>= (idx % CHAR_BIT);
    if(getBit(elements, idx) == 0)
	elements[i] |= m;
    else
        elements[i] ^= m;
}

/*Auxiliar, recibe tamaño en bits*/
void imprime(unsigned char *s, int size) {
    int i;
    for(i = 0; i < size; i++) 
        printf("%d", (int)getBit(s,i));
    printf("%c", '\n');
}

/*Auxiliar, imprime todas las llaves, sizeks es núm de llaves, sizek es tamaño de cada llave*/
void imprime_ks(unsigned char **ks, int sizeks, int sizek) {
    int i;
    for(i = 0; i < sizeks; i++)
        imprime(ks[i], sizek);
    printf("%c", '\n');
}

void imprime_n(unsigned char c) {
    int m = 1;
    m <<= 7;
    while(m > 0) {
        if((c&m) != 0)
            printf("%d", 1);
        else
            printf("%d", 0);
        m >>= 1;
    }
    printf("%c", '\n');
}

//apply function of each element
unsigned char* applyFunction(unsigned char (*f)(unsigned char,unsigned char), unsigned char *l_n, unsigned char *r_n, int size ){
    int idx;
    unsigned char *result = (unsigned char *) malloc (sizeof(unsigned char)*size);
    unsigned char resultFunction ;
    for(idx = 0; idx < size; idx ++) {
        resultFunction = (*f)(getBit(l_n, idx), getBit(r_n, idx));
        //The result of function is 1, 0 because we work with bits
        resultFunction &=1;
        setBit(result, idx, resultFunction);
    }
    return result;
}

// Aplica la permutación específicada, el tamaño se recibe en número de bits
unsigned char* applyPermutation(unsigned char *elements, int *permutation, int size){
    int idx;
    unsigned char *result = (unsigned char *) malloc(ceil((double)size / (double)CHAR_BIT));
    unsigned char resultFunction ;
    for (idx = 0; idx < size; idx++){
        resultFunction = getBit(elements, permutation[idx]-1); //-1 because the matrix idx begin i 1
        //The result of permutation is 1, 0 because we work with bits
        //resultFunction &=1;
        setBit(result, idx, resultFunction);
    }
    return result;
}

// Shift a la izquierda 
unsigned char* l_rotation(unsigned char *elements, int shift, int size){ //size in bits   
    unsigned char *result = (unsigned char *) calloc(size/CHAR_BIT, sizeof(unsigned char));
    int i, j;
    for(i = 0; i < size - shift; i++)
        setBit(result, i, getBit(elements, i + shift));
    for(i = size - shift, j = 0; i < size; i++, j++)
        setBit(result, i, getBit(elements, j));
    return result;
}

// Regresa la subcadena del indice b al indice e, i.e. [b, e). Los índices son de los bits.
unsigned char* substr(unsigned char *str, int b, int e) {
    int size = ceil((double)(e - b) / (double)CHAR_BIT);
    unsigned char *result = (unsigned char *) malloc(size);
    int i;
    for(i = 0; i < e - b; i++)
        setBit(result, i, getBit(str, i + b));
    return result;
}

// Regresa la concatenación de dos cadenas, recibe tamaños de cadenas en bits
unsigned char* catstr(unsigned char *str1, unsigned char *str2, int size1, int size2) {
    unsigned char *result = (unsigned char *) malloc(ceil((double)(size1 + size2) / (double)CHAR_BIT));
    int i, j;
    for(i = 0; i < size1; i++)
        setBit(result, i, getBit(str1, i));
    for(i = size1, j = 0; i < size1 + size2; i++, j++)
        setBit(result, i, getBit(str2, j));
    return result;
}

// Libera la memoria que ocupan las llaves
void free_keys(unsigned char **ks) {
    int i;
    for(i = 0; i < DES_ITERATIONS; i++)
        free(ks[i]);
    free(ks);
}

//Genera las 16 llaves a partir de la llave principal
unsigned char** splitKeys (unsigned char *strk) {
    unsigned char **keys = (unsigned char **) malloc (sizeof(unsigned char*)*DES_ITERATIONS);//Number of keys1
    unsigned char *key = applyPermutation(strk, pc1, 56);
    unsigned char *c = substr(key, 0, 28);
    unsigned char *d = substr(key, 28, 56);
    free(key);
    int i;
    for(i = 0; i < DES_ITERATIONS; i++) {
        unsigned char *c1 = l_rotation(c, noShift[i], 28);
        unsigned char *d1 = l_rotation(d, noShift[i], 28);
        free(c);
        free(d);
        unsigned char *cd = catstr(c1, d1, 28, 28);
        keys[i] = applyPermutation(cd, pc2, 48);
        c = substr(cd, 0, 28);
        d = substr(cd, 28, 56);
        free(cd);
        free(c1);
        free(d1);
    }
    free(c);
    free(d);
    return keys;
}

// Regresa el resultado de aplicar un xor entre a y b
unsigned char xorFunction(unsigned char a, unsigned char b){
    return a^b;
}

// Regresa la representación en char de los bits entre [b, e)
unsigned char getChar(unsigned char* elements, int b, int e) {
    int i, j, res = 0;
    for(i = e - 1, j = 0; i >= b; i--, j++)
        res |= getBit(elements, i) << j;
    return res;
}

// Regresa el número de columna que c especifica
int getCol(unsigned char c) {
    int i, mascara = 1 << 4, res = 0;
    for(i = 0; i < 4; i++) {
        res |= mascara & c;
        mascara >>= 1;
    }
    return res >> 1;
}

// Regresa el número de fila que c especifica
int getRow(unsigned char c) {
    int res = 0;
    if(((1 << 5) & c) != 0)
        res |= 2;
    res |= c & 1;
    return res;
}

//Applies an specific S box
void applySi(unsigned char* elements, unsigned char* result, int i, int* Si) {
    unsigned char c = getChar(elements, (i - 1) * 6, (i - 1) * 6 + 6);
    int s = Si[getCol(c) + getRow(c) * 16];
   int j, m = 1 << 3;
    for(j = 0; j < 4; j++) {
        if((m & s) != 0)
            setBit(result, (i - 1) * 4 + j, 1);
        else
            setBit(result, (i - 1) * 4 + j, 0);
        m >>= 1;
    }
}

//Applies S box
unsigned char* applyS(unsigned char* elements){
    unsigned char *result = (unsigned char *) calloc(4,sizeof(unsigned char));
    applySi(elements, result, 1, S1);
    applySi(elements, result, 2, S2);
    applySi(elements, result, 3, S3);
    applySi(elements, result, 4, S4);
    applySi(elements, result, 5, S5);
    applySi(elements, result, 6, S6);
    applySi(elements, result, 7, S7);
    applySi(elements, result, 8, S8);
    return result;
}

// Aplica la funcion f del algoritmo 
unsigned char* fun(unsigned char *elements, unsigned char* key) {
    unsigned char *e = applyPermutation(elements, E, 48);
    unsigned char *xor = applyFunction(xorFunction, e, key, 48);
    unsigned char *s = applyS(xor);
    unsigned char *result = applyPermutation(s, PRIMITIVE_P, 32);
    free(e);
    free(xor);
    free(s);
    return result;
}

// Función auxiliar que cifra o descifra según se indique con decrypt
unsigned char * encryption_aux(unsigned char *elements, unsigned char* key, int decrypt) { //return cipher text
    unsigned char *ip = applyPermutation(elements, IP, 64);
    unsigned char *l = substr(ip, 0, 32);
    unsigned char *r = substr(ip, 32, 64);
    unsigned char **keys = splitKeys(key);
    free(ip);
    int i, j;
    for(i = 0, j = 15; i < 16; i++, j--) {
        unsigned char *f;
        if(decrypt)
            f = fun(r, keys[j]);
        else
            f = fun(r, keys[i]);
        unsigned char *ls = r;
        unsigned char *rs = applyFunction(xorFunction, l, f, 32);
        free(f);
        free(l);
        l = ls;
        r = rs;
    }
    unsigned char *rl =catstr(r, l, 32, 32);
    unsigned char *result = applyPermutation(rl, IP_INV, 64);
    free(rl);
    free(l);
    free(r);
    free_keys(keys);
    return result;
}

//DES encryption (1 block)
unsigned char * encryption(unsigned char *elements, unsigned char* key) {
    return encryption_aux(elements, key, 0);
}

//Read file and normalize
void encryptionFile(unsigned char *file_name, unsigned char* strK) {
    FILE *fp, *fw;
    int ch;//read character as an int
    fp = fopen((char *)file_name,"r"); // read mode
    fw = fopen("cipher_text.txt", "wb");//write mode
    if( fp == NULL || fw == NULL)  {
        perror("Error while opening the file.\n");
        exit(EXIT_FAILURE);
    }    

    fseek(fp, 0, SEEK_END);
    size_t t = ftell(fp);
    rewind(fp);        
    size_t norm = t + 8 - (t % 8), i;    
    unsigned char *out = malloc(norm);
    for(i = 0; i < t; i++){
      ch = fgetc(fp);
      out[i] = (char) ch;
    }
    
    out[t] = 1 << 7;
    for(i = t + 1; i < norm; i++)
      out[i] = 0;
    out[norm] = '\0';
    
    unsigned char *enc = malloc(0);
    for(i = 0; i < norm * 8; i += 64) {
        unsigned char* block = substr(out, i, i + 64);
        unsigned char* e = encryption(block, strK);
        unsigned char* cat = catstr(enc, e, i, 64);
        free(enc);
        free(block);
        free(e);
        enc = cat;
    }
    
    fwrite((char *)enc, sizeof(*enc), norm, fw);
    free(out);
    free(enc);
    fclose(fp);
    fclose(fw);
}

//DES decrypt (1 block)
unsigned char * decrypt(unsigned char* elements , unsigned char *key) {
    return encryption_aux(elements, key, 1);
}

// Se le quita el padding al archivo de texto claro
unsigned char * rm_padding(unsigned char* plaint, size_t * size) {
    while(getBit(plaint, *size - 1) != 1)
        (*size)--;
    (*size)--;
    size_t i;
    unsigned char *result = malloc(ceil((double)(*size) / (double)CHAR_BIT));
    for(i = 0; i < *size; i++)
        setBit(result, i, getBit(plaint, i));
    return result;
}

//Read file and normalize
void decryptFile(unsigned char *file_name, unsigned char* strK) {
    FILE *fp, *fw;
    int ch; //read character as an int
    fp = fopen((char *)file_name,"rb"); // read mode
    fw = fopen("plain_text.txt", "w");//write mode
    if( fp == NULL || fw == NULL)  {
        perror("Error while opening the file.\n");
        exit(EXIT_FAILURE);
    }

    fseek(fp, 0, SEEK_END);
    size_t t = ftell(fp);
    rewind(fp);        
    unsigned char *out = malloc(t);
    size_t i;
    for(i = 0; i < t; i++){
      ch = fgetc(fp);
      out[i] = (char) ch;
    }
    
    unsigned char *des = malloc(0);
    for(i = 0; i < t * 8; i += 64) {
        unsigned char* block = substr(out, i, i + 64);
        unsigned char* e = decrypt(block, strK);
        unsigned char* cat = catstr(des, e, i, 64);
        free(des);
        free(block);
        free(e);
        des = cat;
    }

    size_t size = t * 8;
    unsigned char *res = rm_padding(des, &size);
    fwrite((char *)res, sizeof(*res), size/8, fw);
    free(out);
    free(des);
    free(res);
    fclose(fp);
    fclose(fw);
}

int main(int argc, char *args[]) {
    if(argc < 4)
        printf("Uso: ./a.out [c|d] <llave> <nombre archivo>\n");
    int i;
    unsigned char* key = (unsigned char*)args[2];
    if(strcmp("c", args[1]) == 0)
        encryptionFile((unsigned char*)args[3], key);
    else if(strcmp("d", args[1]) == 0) 
        decryptFile((unsigned char*)args[3], key);
    else
        printf("Uso: ./a.out [c|d] <llave> <nombre archivo>\n");
    return 0;
}
