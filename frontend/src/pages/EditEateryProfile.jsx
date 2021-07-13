import React, { useContext, useState, useEffect } from "react";
import NavBar from "../components/Navbar";
import { StoreContext } from "../utils/store";
import EateryForm from "../components/EateryForm";
import { useHistory, Redirect } from "react-router";
import { logUserOut } from "../utils/logoutHelper";

export default function EditEateryLanding() {
    const defaultState = { value: "", valid: true };
    const [previewImages, setPreviewImages] = useState([]);
    const [images, setImages] = useState([]);

    const [email, setEmail] = useState(defaultState);

    const [password, setPassword] = useState(defaultState);
    const [confirmPassword, setConfirmPassword] = useState(defaultState);
    const [eateryName, setEateryName] = useState(defaultState);
    const [address, setAddress] = useState(defaultState);
    const [cuisines, setCuisines] = useState({ value: [], valid: true });
    const history = useHistory();

    const context = useContext(StoreContext);
    const [auth, setAuth] = context.auth;
    const [isDiner, setIsDiner] = context.isDiner;
    const setAlertOptions = context.alert[1];

    // set to true for real demos
    const useGoogleAPI = false;

    useEffect(() => {
        // on page init, load the users details
        const getEatery = async () => {
            const response = await fetch(
                "http://localhost:8080/eatery/profile/details",
                {
                    method: "GET",
                    headers: {
                        Accept: "application/json",
                        "Content-Type": "application/json",
                        Authorization: auth,
                    },
                }
            );
            const responseData = await response.json();
            if (response.status === 200) {
                console.log(responseData);
                setEateryName({ value: responseData.name, valid: true });
                setAddress({ value: responseData.address, valid: true });
                setEmail({ value: responseData.email, valid: true });
                setCuisines({ value: responseData.cuisines, valid: true });
                setImages(responseData.menuPhotos);
                setPreviewImages(responseData.menuPhotos);
            } else if (response.status === 401) {
                logUserOut();
            }
        };
        getEatery();
    }, [auth]);

    if (auth === null) return <Redirect to="/" />;
    if (isDiner === "true") return <Redirect to="/DinerLanding" />;

    const updateUser = async () => {
        // check register details
        console.log("register");

        if (email.value === "") setEmail({ value: "", valid: false });
        if (password.value === "") setPassword({ value: "", valid: false });
        if (confirmPassword.value === "")
            setConfirmPassword({ value: "", valid: false });
        if (eateryName.value === "") setEateryName({ value: "", valid: false });
        if (Array.isArray(cuisines.value) && !cuisines.value.length)
            setCuisines({ value: [], valid: false });

        if (useGoogleAPI && address.value === "")
            setAddress({ value: "", valid: false });

        // check that all fields are valid and not empty before updating
        if (
            !email.valid ||
            !password.valid ||
            !confirmPassword.valid ||
            !eateryName.valid ||
            !cuisines.valid ||
            email.value === "" ||
            password.value === "" ||
            confirmPassword.value === "" ||
            eateryName.value === "" ||
            (Array.isArray(cuisines.value) && !cuisines.value.length)
        )
            return;
        if ((!address.valid || address.value === "") && useGoogleAPI) {
            return;
        }
        console.log(images.length);

        const response = await fetch("http://localhost:8080/update/eatery", {
            method: "POST",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
                Authorization: auth,
            },
            body: JSON.stringify({
                alias: eateryName.value,
                email: email.value,
                address: useGoogleAPI ? address.value : "Sydney",
                password: password.value,
                cuisines: cuisines.value,
                menuPhotos: images, // array of data urls
            }),
        });
        console.log(response);
        const responseData = await response.json();
        if (response.status === 200) {
            setAlertOptions({
                showAlert: true,
                variant: "success",
                message: responseData.message,
            });
            history.push("/EateryLanding");
        } else {
            setAlertOptions({
                showAlert: true,
                variant: "error",
                message: responseData.message,
            });
        }
    };

    return (
        <>
            <NavBar isDiner={isDiner} />
            {console.log(eateryName)}
            <EateryForm
                email={email}
                setEmail={setEmail}
                password={password}
                setPassword={setPassword}
                confirmPassword={confirmPassword}
                setConfirmPassword={setConfirmPassword}
                eateryName={eateryName}
                setEateryName={setEateryName}
                address={address}
                setAddress={setAddress}
                cuisines={cuisines}
                setCuisines={setCuisines}
                setImages={setImages}
                previewImages={previewImages}
                setPreviewImages={setPreviewImages}
                isRegister={false}
                submitForm={updateUser}
            />
        </>
    );
}
