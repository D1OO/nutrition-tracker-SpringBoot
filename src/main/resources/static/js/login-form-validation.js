jQuery(document).ready(function ($) {
  "use strict";

  //Contact
  $('form.form').submit(function () {

    var f = $(this).find('.form-group'),
        ferror = false,
        emailExp = /^([a-z0-9_\.-]{1,30})@([\da-z\.-]{1,15})\.([a-z\.]{2,8})$/,
        pwrdExp = /^[0-9a-zA-Z]{8,15}$/i,
        firstNameExp = /^[a-z ,.'-]{2,15}$/i,
        lastNameExp = /^[a-z ,.'-]{2,30}$/i,
        firstNameUaExp = /^[абвгдежзийклмнопрстуфхцчшщьюяіїґєАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЬЮЯІЇЄҐ]{2,20}/i;

    f.children('input').each(function () { // run all inputs

      var i = $(this); // current input
      var rule = i.attr('data-rule');

      if (rule !== undefined) {
        var ierror = false; // error flag for current input
        var pos = rule.indexOf(':', 0);
        if (pos >= 0) {
          var exp = rule.substr(pos + 1, rule.length);
          rule = rule.substr(0, pos);
        } else {
          rule = rule.substr(pos + 1, rule.length);
        }

        switch (rule) {
          case 'required':
            if (i.val() === '') {
              ferror = ierror = true;
            }
            break;

          case 'minlen':
            if (i.val().length < parseInt(exp)) {
              ferror = ierror = true;
            }
            break;

          case 'email':
            if (!emailExp.test(i.val())) {
              ferror = ierror = true;
            }
            break;

          case 'pwrd':
            if (!pwrdExp.test(i.val())) {
              ferror = ierror = true;
            }
            break;

          case 'firstnameexp':
            if (!firstNameExp.test(i.val())) {
              ferror = ierror = true;
            }
            break;

          case 'lastnameexp':
            if (!lastNameExp.test(i.val())) {
              ferror = ierror = true;
            }
            break;

          case 'firstnameuaexp':
            if (i.val() != null)
              if (!firstNameUaExp.test(i.val())) {
                ferror = ierror = true;
              }
            break;

          case 'checked':
            if (!i.is(':checked')) {
              ferror = ierror = true;
            }
            break;

          case 'regexp':
            exp = new RegExp(exp);
            if (!exp.test(i.val())) {
              ferror = ierror = true;
            }
            break;
        }
        i.next('.validate').html((ierror ? (i.attr('data-msg') !== undefined ? i.attr('data-msg') : 'wrong Input') : '')).show('blind');
      }
    });
    f.children('textarea').each(function () { // run all inputs

      var i = $(this); // current input
      var rule = i.attr('data-rule');

      if (rule !== undefined) {
        var ierror = false; // error flag for current input
        var pos = rule.indexOf(':', 0);
        if (pos >= 0) {
          var exp = rule.substr(pos + 1, rule.length);
          rule = rule.substr(0, pos);
        } else {
          rule = rule.substr(pos + 1, rule.length);
        }

        switch (rule) {
          case 'required':
            if (i.val() === '') {
              ferror = ierror = true;
            }
            break;

          case 'minlen':
            if (i.val().length < parseInt(exp)) {
              ferror = ierror = true;
            }
            break;
        }
        i.next('.validate').html((ierror ? (i.attr('data-msg') != undefined ? i.attr('data-msg') : 'wrong Input') : '')).show('blind');
      }
    });
    if (ferror) return false;
    else var str = $(this).serialize();

    var this_form = $(this);
    var action = $(this).attr('action');

    // if( ! action ) {
    //   this_form.find('.loading').slideUp();
    //   this_form.find('.error-message').slideDown().html('The form action property is not set!');
    //   return false;
    // }

    // this_form.find('.sent-message').slideUp();
    // this_form.find('.error-message').slideUp();
    // this_form.find('.loading').slideDown();

    this_form.submit();
  });

});
