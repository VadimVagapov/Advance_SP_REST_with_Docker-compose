const url = 'http://localhost:8080/api/users/';
const urlPrincipal = 'http://localhost:8080/api/user';
const editUserForm = document.getElementById("editModalForm");
const deleteUserForm = document.getElementById("deleteModalForm");
const createUserForm = document.getElementById("createUserForm");


//получить всех пользователей для таблицы пользователей
getAllUsers();

async function getAllUsers() {
    const responseGet = await fetch(url, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        }
    });
    console.log(responseGet);
    const user = await responseGet.json();
    console.log(user);

    length = user.length;
    console.log(user);
    let temp = "";
    for (i = 0; i < length; i++) {
        temp += `<tr>
        <td>${user[i].id}</td>
        <td>${user[i].username}</td>
        <td>${user[i].lastname}</td>
        <td>${user[i].age}</td>
        <td>${user[i].email}</td>
        <td>${stringRoles(user[i])}</td>
        <td><button type="button" class="btn btn-info" className data-bs-toggle="modal"  data-bs-target="#editModal" onClick="editModal(${user[i].id})">Edit</button></td>
        <td><button type="button" class="btn btn-danger" className data-bs-toggle="modal" data-bs-target="#deleteModal" onClick="deleteModal(${user[i].id})">Delete</button></td>
        </tr>`
    }
    document.getElementById("allUsers").innerHTML = temp;
}

//получение пользователя для редактирования
async function editModal(id) {
    await fetch(url + id, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        }
    })
        .then(res => {
            res.json()
                .then(async user => {
                    console.log(user);
                    document.getElementById('idEdit').value = user.id;
                    document.getElementById('firstNameEdit').value = user.username;
                    document.getElementById('lastNameEdit').value = user.lastname;
                    document.getElementById('ageEdit').value = user.age;
                    document.getElementById('emailEdit').value = user.email;
                    document.getElementById('passwordEdit').value = user.password;
                    if (user.roles.length === 2) {
                        document.getElementById('adminEdit').setAttribute('selected', 'true');
                        document.getElementById('userEdit').setAttribute('selected', 'true');
                    } else if (user.roles[0].id === 1) {
                        document.getElementById('adminEdit').setAttribute('selected', 'true');
                        document.getElementById('userEdit').setAttribute('selected', 'false');
                    } else if (user.roles[0].id === 2) {
                        document.getElementById('userEdit').setAttribute('selected', 'true');
                        document.getElementById('adminEdit').setAttribute('selected', 'false');
                    }
                })
        })
}

//редактирование пользователя
editUserForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    let idValue = document.getElementById("idEdit").value;
    let firstnameEdit = document.getElementById("firstNameEdit").value;
    let lastnameEdit = document.getElementById("lastNameEdit").value;
    let ageEdit = document.getElementById("ageEdit").value;
    let emailEdit = document.getElementById("emailEdit").value;
    let passwordEdit = document.getElementById("passwordEdit").value;
    let rolesEdit = getAllRoles(Array.from(document.getElementById("rolesEdit").selectedOptions).map(role => role.value));

    fetch(url, {
        method: "PUT",
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        },
        body: JSON.stringify({
            id: idValue,
            username: firstnameEdit,
            lastname: lastnameEdit,
            age: ageEdit,
            email: emailEdit,
            password: passwordEdit,
            roles: rolesEdit
        })
    })
        .then(() => {
            document.getElementById("nav-table-tab").click();
            getAllUsers();
            document.getElementById("closeEdit").click();
        })
});

//получение данных пользователя перед удалением
async function deleteModal(id) {

    await fetch(url + id, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        }
    })
        .then(res => {
            res.json()
                .then(async user => {
                    console.log(user);
                    document.getElementById('idDelete').value = user.id;
                    document.getElementById('userNameDelete').value = user.username;
                    document.getElementById('lastNameDelete').value = user.lastname;
                    document.getElementById('ageDelete').value = user.age;
                    document.getElementById('emailDelete').value = user.email;
                    document.getElementById('passwordDelete').value = user.password;

                    if (user.roles.length === 2) {
                        document.getElementById('roleAdminDelete').setAttribute('selected', 'true');
                        document.getElementById('roleUserDelete').setAttribute('selected', 'true');
                    } else if (user.roles[0].authority.equals('ROLE_ADMIN')) {
                        document.getElementById('roleAdminDelete').setAttribute('selected', 'true');
                        document.getElementById('roleUserDelete').setAttribute('selected', 'false');
                    } else if(user.roles[0].authority.equals('ROLE_USER')) {
                        document.getElementById('roleUserDelete').setAttribute('selected', 'true');
                        document.getElementById('roleAdminDelete').setAttribute('selected', 'false');
                    }
                })
        })
}

//удаление пользователя
deleteUserForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    let idValue = document.getElementById("idDelete").value;

    fetch(url + idValue, {
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        }
    })
        .then(() => {
            document.getElementById("nav-table-tab").click();
            getAllUsers();
            document.getElementById("closeDelete").click();
        })
});

//создание нового пользователя
createUserForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    let usernameNew = document.getElementById("username").value;
    let lastnameNew = document.getElementById("lastname").value;
    let ageNew = document.getElementById("age").value;
    let emailNew = document.getElementById("email").value;
    let passwordNew = document.getElementById("password").value;
    let rolesNew = getAllRoles(Array.from(document.getElementById("selectrole").selectedOptions).map(role => role.value));

    fetch(url, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        },
        body: JSON.stringify({
            id: null,
            username: usernameNew,
            lastname: lastnameNew,
            age: ageNew,
            email: emailNew,
            password: passwordNew,
            roles: rolesNew
        })
    })
        .then(() => {
            document.getElementById("nav-table-tab").click();
            getAllUsers();
        })
});

//для шапки авторизации
getAuthoritesUser()
async function getAuthoritesUser() {
    const response = await fetch(urlPrincipal, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        }
    })
        .then((res) => res.json())
        .then((user) => {
            document.getElementById("authRoles").innerHTML = stringRoles(user);
            document.getElementById("authUserName").innerHTML = user.username;
        })
}

//получение коротких ролей в одной строке
 function stringRoles(user) {
     let roles = '';
     let lengthRole = user.roles.length
     for (j = 0; j < lengthRole; j++) {
         roles += user.roles[j].authority.replace('ROLE_', '').concat(' ')
     }
     return roles;
 }

 //получение ролей для нового пользователя и для редактирования уже существующего
function getAllRoles(role) {
    let roles = [];
    if (role.indexOf("ADMIN") >= 0) {
        roles.push({
            "id": 1,
            "authority": 'ROLE_ADMIN'
        });
    }
    if (role.indexOf("USER") >= 0) {
        roles.push({
            "id": 2,
            "authority": 'ROLE_USER'
        });
    }
    return roles;
}









