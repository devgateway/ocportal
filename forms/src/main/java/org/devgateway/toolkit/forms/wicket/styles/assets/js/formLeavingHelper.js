/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the MIT License (MIT) which accompanies this
 * distribution, and is available at https://opensource.org/licenses/MIT
 *
 * Contributors: Development Gateway - initial API and implementation
 ******************************************************************************/
// if nothing has changed that the user can leave the page without any confirmation
var shouldConfirmFormLeaving = false;

$(document).ready(function () {
    $(':input').each(function () {
        $(this).on('change', function () {
            shouldConfirmFormLeaving = true;
        });
    });

    // set shouldConfirmFormLeaving when input has focus
    $(':input').each(function () {
        $(this).on('keyup', function () {
            shouldConfirmFormLeaving = true;
        });
    });

    $(document).on('drop', 'textarea', function (e) {
        // force focus on that element
        $(this).focus();

        var that = $(this);

        // when the drop happens the input value will not be updated yet
        // use a timeout to allow the input value to change.
        setTimeout(function () {
            that.trigger('change');
        }, 50);

        shouldConfirmFormLeaving = true;
    });

    $(document).on('drop', ':input', function (e) {
        // force focus on that element
        $(this).focus();

        var that = $(this);

        // when the drop happens the input value will not be updated yet
        // use a timeout to allow the input value to change.
        setTimeout(function () {
            that.trigger('change');
        }, 50);

        shouldConfirmFormLeaving = true;
    });
});


// confirmation modal before window unload
$(window).on('beforeunload', function (event) {
    if (shouldConfirmFormLeaving) {
        // Cancel the event as stated by the standard.
        event.preventDefault();

        // Chrome requires returnValue to be set.
        var message = "There is unsaved text that will be lost if you leave without saving. Would you like to save the form or exit without saving?";
        event.returnValue = message;

        return message;
    }
});

function enableFormLeavingConfirmation() {
    shouldConfirmFormLeaving = true;
}

function disableFormLeavingConfirmation() {
    shouldConfirmFormLeaving = false;
}
