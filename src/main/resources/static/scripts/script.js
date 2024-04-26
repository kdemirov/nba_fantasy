let FORM_SELECTED_OPTIONS

function showModal(elementsAttr, formSelectedOptions) {
    $('#exampleModal').modal()

    if (!checkIfValueIsSet()) {
        FORM_SELECTED_OPTIONS = formSelectedOptions
    }

    $('#exampleModal').on('shown.bs.modal', function (event) {
        event.preventDefault()

        //special case for teamId while pulling players for the team with the id
        if (FORM_SELECTED_OPTIONS.has('teamId')) {
            $("#teamId").val(FORM_SELECTED_OPTIONS.get('teamId'))
        }

        fillFormSelectOptionValues(elementsAttr)
        fillSelectedElementValues(elementsAttr)
    })

    $('#exampleModal').one('hidden.bs.modal', function (event) {
        event.preventDefault()
        if (document.getElementsByTagName('elementAttributes')) {
            $('#elementAttributes').empty()
        }
        for (let [key, value] of FORM_SELECTED_OPTIONS.entries()) {
            if (!value) {
                $('#' + key).empty()
            }
        }
        $('#exampleModal').modal('dispose')
    })
}

function checkIfValueIsSet() {
    if (FORM_SELECTED_OPTIONS) {
        for (let value of FORM_SELECTED_OPTIONS.values()) {
            if (value) {
                return true
            }
        }
    }
    return false
}

function fillFormSelectOptionValues(elementsAttr) {
    for (let i = 0; i < elementsAttr.length; i++) {
        let optionNode = document.createElement("option")
        let textNode = document.createTextNode(elementsAttr[i].value)
        optionNode.appendChild(textNode)
        let valueAttr = document.createAttribute("value")
        valueAttr.value = elementsAttr[i].value;
        optionNode.setAttributeNode(valueAttr)
        let elements = document.getElementsByTagName("select")
        elements = Array.from(elements)
        elements.forEach(el => el.appendChild(optionNode.cloneNode(true)))
    }
}

function fillSelectedElementValues(elementsAttr) {
    let selectedElementRow = document.getElementById('elementAttributes')
    if (selectedElementRow) {
        for (let i = 0; i < elementsAttr.length; i++) {
            selectedElementRow.appendChild(createDivElement(elementsAttr[i].name))
            selectedElementRow.appendChild(createDivElement(elementsAttr[i].value))
        }
    }
}

function createDivElement(textNodeValue) {
    let divElement = document.createElement("div")
    let classAttr = document.createAttribute("class")
    classAttr.value = "col-sm-6"
    divElement.setAttributeNode(classAttr)
    let attKeyTextNode = document.createTextNode(textNodeValue)
    divElement.appendChild(attKeyTextNode)
    return divElement
}

function onChange(select) {
    let changedOptions = new Map()
    for (let [key, value] of FORM_SELECTED_OPTIONS.entries()) {
        if (select.name === key) {
            changedOptions.set(key, select.value)
        } else {
            changedOptions.set(key, value)
        }
    }
    FORM_SELECTED_OPTIONS = changedOptions
}

function saveForm() {
    let error = false
    for (let [key, value] of FORM_SELECTED_OPTIONS.entries()) {
        if (!value) {
            error = true
            alert("You must select valid options for all fields")
        } else {
            //for game modal if the game is not finished points from the teams and game details url should be selected blank field
            if (value === ' ') {
                $(`#${key}`).empty()
            }
        }
    }
    if (!error) {
        document.getElementById("saveForm").submit()
    }
}