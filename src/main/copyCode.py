import os

def concatenar_archivos(directorio_actual, archivo_salida):
    # Obtener la lista de archivos en el directorio actual
    archivos_en_directorio = os.listdir("F:\Compiler\src\main")

    # Filtrar solo los archivos con extensión .java
    archivos_java = [archivo for archivo in archivos_en_directorio if archivo.endswith('.java') and archivo != archivo_salida]

    # Abrir el archivo de salida en modo escritura
    with open(archivo_salida, 'w') as archivo_final:
        # Iterar sobre cada archivo Java y escribir su contenido en el archivo final
        for archivo in archivos_java:
            with open(os.path.join(directorio_actual, archivo), 'r') as archivo_actual:
                contenido = archivo_actual.read()
                archivo_final.write(contenido)
                # Agregar un salto de línea al final del contenido de cada archivo
                archivo_final.write('\n')

if __name__ == "__main__":
    # Directorio actual
    directorio_actual = "F:\Compiler\src\main"

    # Nombre del archivo de salida
    archivo_salida = 'codigo_concatenado.txt'

    # Llamar a la función para concatenar los archivos
    concatenar_archivos(directorio_actual, archivo_salida)

    print(f"Se ha creado el archivo {archivo_salida} con el código de todos los archivos Java en el directorio (excepto el propio script).")
