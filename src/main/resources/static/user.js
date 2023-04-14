const urlPrincipal = 'http://localhost:8080/api/user';

getAuthoritesUser();
async function getAuthoritesUser() {
    let roles = '';
    const response = await fetch(urlPrincipal, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        }
    })
        .then((res) => res.json())
        .then((user) => {
            let temp = '';
            let lengthRole = user.roles.length
            for (j = 0; j < lengthRole; j++) {
                roles += user.roles[j].authority.replace('ROLE_', '').concat(' ')
            }
            temp += `<tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.lastname}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td>${roles}</td>
            </tr>`

            document.getElementById("authUser").innerHTML = temp;
            document.getElementById("authRoles").innerHTML = roles;
            document.getElementById("authUserName").innerHTML = user.username;
        })

    let getContent = document.getElementById('hasRole');
    let content;

    if  (roles.indexOf("ADMIN") >= 0) {
        content = `<ul class="nav nav-pills flex-column mb-auto" id="navbar_principal">
                    <li class="nav-item">
                        <a th:href="@{/admin}" class="nav-link link-dark" >Admin</a></li>
                    <li class="nav-item">
                        <a th:href="@{/user}" class="nav-link active">User</a></li></ul>`;
    }
    else {
        content = `<ul class="nav nav-pills flex-column mb-auto" id="navbar_principal">
                    <li class="nav-item">
                        <a th:href="@{/user}" class="nav-link active">User</a></li></ul>`;
    }

    getContent.insertAdjacentHTML('afterbegin', content);
}


