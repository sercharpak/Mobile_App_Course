/*
 * Copyright 2011 Research In Motion Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

function removeLocalItem(key) {
    try {
        localStorage.removeItem(key);
        mostrarIngredientes();
    }
    catch (e) {
        debug.log("removeLocalItem", e, debug.exception);
    }
}


function agregarIngrediente() {
    //Se recuperan todos los valores del ingrediente
    var nombre = document.getElementById("txtNombreIngrediente").value;
    var costo= document.getElementById("txtCosto").value;
    var unidadesDisp= document.getElementById("txtUnidades").value;
    var unidadMedida= document.getElementById("txtUnidad").value;
    //Variable que servirá para identificar qué tipo de objeto es
    var tipo="ingrediente";
    if ((nombre !== "") && (costo !== "")&& (unidadesDisp!=="")) {
        //Se verifica que el costo tenga un valor válido

        if(isFinite(costo) && isFinite(unidadesDisp) && costo>0){
            if (window.localStorage) {
                var descripcionIngrediente= new Array(4);
                descripcionIngrediente[0] = tipo;
                descripcionIngrediente[1] = costo;
                descripcionIngrediente[2] = unidadesDisp;
                descripcionIngrediente[3]= unidadMedida;
                var stringified = JSON.stringify(descripcionIngrediente);
                //El nombre permitirá identificar el ingrediente.
                window.localStorage.setItem(nombre, stringified);
                window.alert("Se agregó el ingrediente correctamente. Ahora tienes el ingrediente "+ nombre + " en tu despensa.");
            } else {
                //Local storage is not supported - save the key/value in a cookie instead?
                alert("window.localStorage API is not supported.");
                return false;
            }
        }else{
            alert("El costo o las unidades disponibles no tienen valores válidos");
            return false;
        }
    }else{
        alert("El nombre, costo y las unidades disponibles deben tener valores válidos");
        return false;
    }
}

function mostrarIngredientes(){
    try {

        var num, out, i, tipo, nombre, toParse, descripcionIngrediente, costo, unidadesDisp, unidadMedida;
        if (window.localStorage) {
            num = window.localStorage.length;
            if (num > 0) {
                out = "<table width='100%' cellspacing='0' cellpadding='0' border='1' bordercolor='#919DE6'>";
                out += "<tr><th>Nombre</th><th>Costo</th><th>Unidades disponibles</th><th>Unidad de Medida</th><th>Borrar</th></tr>";
                for (i = 0; i < num; i = i + 1) {
                    nombre = window.localStorage.key(i);
                    toParse = window.localStorage.getItem(nombre);
                    descripcionIngrediente = JSON.parse(toParse);
                    tipo = descripcionIngrediente[0];
                    if(tipo == "ingrediente"){
                        costo= descripcionIngrediente[1];
                        unidadesDisp= descripcionIngrediente[2];
                        unidadMedida= descripcionIngrediente[3];
                        if(unidadesDisp==-1){
                            unidadesDisp="Suficiente";
                        }
                        out += "<tr><td>" + nombre + "</td><td>" + costo + "</td><td>" + unidadesDisp + "</td><td>"+unidadMedida+"</td><td><button onclick=\"removeLocalItem('" + nombre + "')\">Eliminar</button></td></tr>";
                    }
                }
                out += "</table>";
                var  divIngredientes  = document.getElementById('ingredientes');
                if(out == ""){
                    out = "No hay ingredientes actualmente";
                }
                divIngredientes.innerHTML= out;
                show("btnClearLocal");

            }
            else {
                setContent("ingredientes", "<i>No hay Ingredientes</i>");
                hide("btnClearLocal");
            }
        }
        else {
            //Local storage is not supported - read the values from cookies instead?
            setContent("ingredientes", "<i><b>window.localStorage</b> API is not supported.</i>");
            return false;
        }
    }
    catch (e) {
        debug.log("displayStorage", e, debug.exception);
    }
}
/**
 * The storage event is fired on the same window object whenever stored data changes as a result of calling setItem(), removeItem() or clear().
 */
function handleStorageEvent(storage) {
    try {
        var key = storage.key;
        var oldValue = storage.oldValue;
        var newValue = storage.newValue;
        var url = storage.url;
        prependContent("output", "handleStorageEvent: changing '" + oldValue + "' to '" + newValue + "' for key '" + key + "' (" + url + ")<br/>");
		
        debug.log("handleStorageEvent", "Complete", debug.info);
    }
    catch (e) {
        debug.log("handleStorageEvent", e, debug.exception);
    }
}

function doPageLoad() {
    try  {
        if (!window.localStorage) {
            prependContent("output", "window.localStorage API not supported<br/>");
            document.getElementById("localRadio").setAttribute("disabled", "true");
            document.getElementById("btnClearLocal").setAttribute("disabled", "true");
        }
        hide("btnClearLocal");

        if (!window.sessionStorage) {
            prependContent("output", "window.sessionStorage API not supported<br/>");
            document.getElementById("sessionRadio").setAttribute("disabled", "true");
            document.getElementById("btnClearSession").setAttribute("disabled", "true");
        }
        hide("btnClearSession");
		
        displayStorage();

        //don't think this is doing anything
        if (window.addEventListener) {
            window.addEventListener("storage", handleStorageEvent, false);
        }
        else {
            window.attachEvent("onstorage", handleStorageEvent);
        }

    }
    catch (e) {
        debug.log("initPage", e.message, debug.exception);
    }
}

window.addEventListener("load", doPageLoad, false);