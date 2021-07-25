import React, { useState, useContext } from "react";
import { useHistory, Redirect } from "react-router";
import { StoreContext } from "../utils/store";
import EateryForm from "../components/EateryForm";
import EateryTempPic from "../assets/EateryTempPic.jpg";

export default function RegisterEatery() {
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
    const setAlertOptions = context.alert[1];
    const [auth, setAuth] = context.auth;
    const [isDiner, setIsDiner] = context.isDiner;

    // set to true for real demos
    const useGoogleAPI = false;

    const registerUser = async () => {
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

        // check that all fields are valid and not empty before registering
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
        console.log(images);
        console.log(cuisines);
        const response = await fetch("http://localhost:8080/register/eatery", {
            method: "POST",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                alias: eateryName.value,
                email: email.value,
                address: useGoogleAPI ? address.value : "Sydney",
                password: password.value,
                cuisines: cuisines.value,
                menuPhotos: images, // array of data urls
                profilePic: EateryTempPic,
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
            setAuth(responseData.data.token);
            localStorage.setItem("token", responseData.data.token);
            localStorage.setItem("isDiner", "false");
            setIsDiner("false");
            history.push("/EateryLanding");
        } else {
            setAlertOptions({
                showAlert: true,
                variant: "error",
                message: responseData.message,
            });
        }
    };

    if (isDiner === "true" && auth !== null)
        return <Redirect to="/DinerLanding" />;
    if (isDiner === "false" && auth !== null)
        return <Redirect to="/EateryLanding" />;

    return (
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
            isRegister={true}
            submitForm={registerUser}
        />
    );
}
