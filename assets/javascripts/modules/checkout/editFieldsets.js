define([
    'bean',
    'modules/checkout/formElements',
    'modules/checkout/impressionTracking'
], function (
    bean,
    formEls,
    impressionTracking) {
    'use strict';

    var FIELDSET_COMPLETE = 'is-complete';
    var FIELDSET_COLLAPSED = 'is-collapsed';

    function collapseFieldsetsExcept(leaveOpen) {
        [
            formEls.$FIELDSET_YOUR_DETAILS,
            formEls.$FIELDSET_DELIVERY_DETAILS,
            formEls.$FIELDSET_BILLING_ADDRESS,
            formEls.$FIELDSET_PAYMENT_DETAILS,
            formEls.$FIELDSET_REVIEW
        ].forEach(function(item) {
            if (!item.length) {
                return;
            }
            if (item === leaveOpen) {
                item.removeClass(FIELDSET_COLLAPSED);
            } else {
                item.addClass(FIELDSET_COLLAPSED);
            }
        });
    }

    function init() {
        var $editDetails = formEls.$EDIT_YOUR_DETAILS;
        var $editPayment = formEls.$EDIT_PAYMENT_DETAILS;
        var $editDelivery = formEls.$EDIT_DELIVERY_DETAILS;
        var $editBilling = formEls.$EDIT_BILLING_ADDRESS;

        if ($editDetails.length && $editPayment.length) {
            bean.on($editDetails[0], 'click', function(e) {
                e.preventDefault();
                collapseFieldsetsExcept(formEls.$FIELDSET_YOUR_DETAILS);
                formEls.$FIELDSET_YOUR_DETAILS.removeClass(FIELDSET_COMPLETE);
                formEls.$FIELDSET_PAYMENT_DETAILS.removeClass(FIELDSET_COMPLETE);
                formEls.$NOTICES.attr('hidden', true);
                impressionTracking.personalDetailsTracking();
            });

            bean.on($editPayment[0], 'click', function(e) {
                e.preventDefault();
                collapseFieldsetsExcept(formEls.$FIELDSET_PAYMENT_DETAILS);
                formEls.$FIELDSET_PAYMENT_DETAILS.removeClass(FIELDSET_COMPLETE);
                impressionTracking.paymentDetailsTracking();
            });

            if ($editDelivery.length) {
                bean.on($editDelivery[0], 'click', function(e) {
                    e.preventDefault();
                    collapseFieldsetsExcept(formEls.$FIELDSET_DELIVERY_DETAILS);
                    formEls.$FIELDSET_DELIVERY_DETAILS.removeClass(FIELDSET_COMPLETE);
                    impressionTracking.deliveryDetailsTracking();
                });
            }

            if ($editBilling.length) {
                bean.on($editBilling[0], 'click', function(e) {
                    e.preventDefault();
                    collapseFieldsetsExcept(formEls.$FIELDSET_BILLING_ADDRESS);
                    formEls.$FIELDSET_BILLING_ADDRESS.removeClass(FIELDSET_COMPLETE);
                    impressionTracking.billingDetailsTracking();
                });
            }
        }
    }

    return {
        init: init
    };

});
