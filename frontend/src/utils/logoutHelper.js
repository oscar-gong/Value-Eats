export function logUserOut() {  
    localStorage.removeItem("token");
    localStorage.removeItem("isDiner");
    window.location = "/";
}
