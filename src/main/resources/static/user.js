const urlPrincipal = 'http://localhost:8080/api/user';

getAuthoritesUser();
async function getAuthoritesUser() {
    const response = await fetch(urlPrincipal, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        }
    })
        .then((res) => res.json())
        .then((user) => {
            let temp = '';
            let roles = '';
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
}

function thisIsAdmin() {
    if(roles.indexOf('ADMIN') >= 0) {
        return true;
    } else {
        return false;
    }
}
