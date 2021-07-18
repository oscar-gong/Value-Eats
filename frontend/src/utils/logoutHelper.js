export function logUserOut(setAuth, setIsDiner) {  
    localStorage.removeItem("token");
    localStorage.removeItem("isDiner");
    setAuth(false);
    setIsDiner(false);
    window.location = "/";
}
