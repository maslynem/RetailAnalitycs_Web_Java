function confirmDelete(t) {
    let answer = confirm("Are you sure you want to do delete a record?");
    if (answer) {
        t.form.submit();
    }
}