import React from "react";
import { FloatBox } from "../styles/FloatBox";
import { Subtitle } from "../styles/Subtitle";
import { FileUpload } from "../styles/FileUpload";
import { Label } from "../styles/Label";
import { ImagePreview } from "../styles/ImagePreview";
import { AlignCenter } from "../styles/AlignCenter";
import { Box, TextField, Button } from "@material-ui/core";
import SendIcon from "@material-ui/icons/Send";
import AddAPhotoIcon from "@material-ui/icons/AddAPhoto";
import { checkValidEmail, checkValidPassword, fileToDataUrl } from "./helpers";
import { usePlacesWidget } from "react-google-autocomplete";
import { useHistory } from "react-router";
import Autocomplete from "@material-ui/lab/Autocomplete";

export default function RegisterEatery() {
    const defaultState = { value: "", valid: true };
    const [previewImages, setPreviewImages] = React.useState([]);
    const [images, setImages] = React.useState([]);

    const [email, setEmail] = React.useState(defaultState);

    const [password, setPassword] = React.useState(defaultState);
    const [confirmPassword, setConfirmPassword] = React.useState(defaultState);
    const [eateryName, setEateryName] = React.useState(defaultState);
    const [address, setAddress] = React.useState(defaultState);
    const [cuisines, setCuisines] = React.useState({ value: [], valid: true });
    const history = useHistory();

    // set to true for real demos
    const useGoogleAPI = false;

    const handleImages = (data) => {
        Array.from(data).forEach((file) => {
            fileToDataUrl(file).then((url) => {
                setImages((prevArray) => [...prevArray, url]);
            });
        });

        if (data) {
            const fileArray = Array.from(data).map((file) =>
                URL.createObjectURL(file)
            );
            setPreviewImages(fileArray);
        }
    };

    const getPreviewImages = (data) => {
        return data.map((photo) => {
            return <ImagePreview src={photo} key={photo} />;
        });
    };

    const handleKeyPress = (e) => {
        if (e.key === "Enter") {
            registerUser();
        }
    };

    const validEmail = () => {
        if (!checkValidEmail(email.value))
            setEmail({ values: "", valid: false });
    };

    const validPassword = () => {
        if (!checkValidPassword(password.value))
            setPassword({ values: "", valid: false });
    };

    const validConfirmPassword = () => {
        if (
            password.value !== confirmPassword.value ||
            confirmPassword.value === ""
        ) {
            setConfirmPassword({ values: "", valid: false });
        }
    };

    const validEateryName = () => {
        if (eateryName.value === "") {
            setEateryName({ value: "", valid: false });
        }
    };

    const validAddress = () => {
        if (address.value === "") {
            setAddress({ values: "", valid: false });
        }
    };

    const validCuisine = () => {
        console.log(cuisines.value);
        if (Array.isArray(cuisines.value) && !cuisines.value.length) {
            setCuisines({ values: [], valid: false });
        }
    };

    const { ref } = usePlacesWidget({
        apiKey: "AIzaSyCG80LxbPTd4MNoZuPdzbF-aQA_DcCAGVQ",
        onPlaceSelected: (place) =>
            setAddress({ value: place.formatted_address, valid: true }),
        options: {
            types: ["address"],
            componentRestrictions: { country: "au" },
        },
    });

    // temp list
    const cuisineList = ["italian", "japanese", "american"];

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
                name: eateryName.value,
                email: email.value,
                address: useGoogleAPI ? address.value : "Sydney",
                password: password.value,
                cuisines: cuisines.value,
                menuPhotos: images, // array of data urls
            }),
        });
        console.log(response);
        if (response.status === 200) {
            history.push("/EateryLanding");
        } else if (response.status === 409) {
            alert("Email already exists - please try another one");
        } else {
            alert("Something went wrong");
        }
    };

    return (
        <AlignCenter>
            <FloatBox display="flex" flexDirection="column" alignItems="center">
                <Box pt={2}>
                    <Subtitle>Register Eatery</Subtitle>
                </Box>
                <Box pt={2} width="60%">
                    <TextField
                        id="outlined-basic"
                        label="Email Address"
                        onChange={(e) =>
                            setEmail({ value: e.target.value, valid: true })
                        }
                        onBlur={validEmail}
                        error={!email.valid}
                        helperText={
                            email.valid ? "" : "Please enter a valid email"
                        }
                        variant="outlined"
                        fullWidth
                    />
                </Box>
                <Box pt={2} width="60%">
                    <TextField
                        id="outlined-basic"
                        label="Password"
                        type="password"
                        onChange={(e) =>
                            setPassword({ value: e.target.value, valid: true })
                        }
                        onBlur={validPassword}
                        error={!password.valid}
                        helperText={
                            password.valid
                                ? ""
                                : "Please enter a valid password with 1 lowercase, 1 upper case, 1 number with at least 8 characters"
                        }
                        variant="outlined"
                        fullWidth
                    />
                </Box>
                <Box pt={2} width="60%">
                    <TextField
                        id="outlined-basic"
                        label="Confirm Password"
                        type="password"
                        onChange={(e) =>
                            setConfirmPassword({
                                value: e.target.value,
                                valid: true,
                            })
                        }
                        onBlur={validConfirmPassword}
                        error={!confirmPassword.valid}
                        helperText={
                            confirmPassword.valid
                                ? ""
                                : "Please make sure your passwords match"
                        }
                        variant="outlined"
                        fullWidth
                    />
                </Box>
                <Box pt={2} width="60%">
                    <TextField
                        id="outlined-basic"
                        label="Eatery Name"
                        onChange={(e) =>
                            setEateryName({
                                value: e.target.value,
                                valid: true,
                            })
                        }
                        onBlur={validEateryName}
                        error={!eateryName.valid}
                        helperText={
                            eateryName.valid
                                ? ""
                                : "Please enter the name of your eatery"
                        }
                        variant="outlined"
                        fullWidth
                    />
                </Box>
                <Box pt={2} width="60%">
                    <TextField
                        id="outlined-basic"
                        disabled={!useGoogleAPI}
                        onBlur={validAddress}
                        onChange={(e) =>
                            setAddress({ value: e.target.value, valid: true })
                        }
                        error={!address.valid}
                        helperText={
                            address.valid
                                ? ""
                                : "Please enter the address of your eatery"
                        }
                        fullWidth
                        variant="outlined"
                        inputRef={ref}
                    />
                </Box>
                <Box pt={2} width="60%">
                    <Autocomplete
                        multiple
                        id="tags-outlined"
                        options={cuisineList}
                        onChange={(e, allOptions) =>
                            setCuisines({ value: allOptions, valid: true })
                        }
                        onBlur={validCuisine}
                        filterSelectedOptions
                        renderInput={(params) => (
                            <TextField
                                id="outlined-basic"
                                {...params}
                                variant="outlined"
                                placeholder="Select Cuisines"
                                error={!cuisines.valid}
                                helperText={
                                    cuisines.valid
                                        ? ""
                                        : "Please select cuisines"
                                }
                                fullWidth
                            />
                        )}
                    />
                </Box>
                <Box pt={1}>
                    <Label>
                        <FileUpload
                            type="file"
                            multiple
                            onChange={(e) => handleImages(e.target.files)}
                        />
                        {<AddAPhotoIcon />} Upload Menu Photos
                    </Label>
                </Box>
                <Box flex-wrap="wrap" flexDirection="row" width="60%">
                    {getPreviewImages(previewImages)}
                </Box>
                <Box pt={4} pb={4}>
                    <Button
                        variant="contained"
                        color="primary"
                        endIcon={<SendIcon />}
                        onKeyPress={handleKeyPress}
                        onClick={registerUser}
                    >
                        Register
                    </Button>
                </Box>
            </FloatBox>
        </AlignCenter>
    );
}
