import React, { useState, useEffect } from "react";
import UploadPhotos from "./UploadPhotos";
import { FloatBox } from "../styles/FloatBox";
import { Subtitle } from "../styles/Subtitle";
import { AlignCenter } from "../styles/AlignCenter";
import { Box, TextField } from "@material-ui/core";
import SendIcon from "@material-ui/icons/Send";
import {
  validEmail,
  validConfirmPassword,
  validPassword,
  validRequired
} from "../utils/helpers";
import { usePlacesWidget } from "react-google-autocomplete";
import Autocomplete from "@material-ui/lab/Autocomplete";
// import { StoreContext } from "../utils/store";
import { ButtonStyled } from "../styles/ButtonStyle";

export default function EateryForm ({
  email,
  setEmail,
  password,
  setPassword,
  confirmPassword,
  setConfirmPassword,
  eateryName,
  setEateryName,
  address,
  setAddress,
  cuisines,
  setCuisines,
  setImages,
  previewImages,
  setPreviewImages,
  isRegister,
  submitForm,
  removeBg = false
}) {
  // const context = useContext(StoreContext);
  // const setAlertOptions = context.alert[1];
  // const token = context.auth[0];
  const [cuisineList, setCuisineList] = useState([]);
  // set true for demos
  const useGoogleAPI = false;

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

  useEffect(() => {
    const listOfCuisines = async () => {
      const response = await fetch(
        "http://localhost:8080/list/cuisines",
        {
          method: "GET",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
          },
        }
      );

      const responseData = await response.json();
      if (response.status === 200) {
        setCuisineList(responseData.cuisines);
      }
    };
    listOfCuisines();
  }, []);

  return (
        <AlignCenter removeBg={removeBg}>
            <FloatBox display="flex" flexDirection="column" alignItems="center">
                <Box pt={2}>
                    <Subtitle>
                        {isRegister === true
                          ? "Register Eatery"
                          : "Update Eatery"}
                    </Subtitle>
                </Box>
                <Box pt={2} width="60%">
                    <TextField
                        id="outlined-basic"
                        label="Email Address"
                        value={email.value}
                        onChange={(e) =>
                          setEmail({ value: e.target.value, valid: true })
                        }
                        onBlur={() => validEmail(email, setEmail)}
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
                        onBlur={() => validPassword(password, setPassword)}
                        onChange={(e) =>
                          setPassword({ value: e.target.value, valid: true })
                        }
                        error={(!password.valid && isRegister) || (!isRegister && !password.valid && password.value.length !== 0)}
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
                        onBlur={() =>
                          validConfirmPassword(
                            password,
                            confirmPassword,
                            setConfirmPassword
                          )
                        }
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
                        value={eateryName.value}
                        onChange={(e) =>
                          setEateryName({
                            value: e.target.value,
                            valid: true,
                          })
                        }
                        onBlur={() => validRequired(eateryName, setEateryName)}
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
                        value={address.value}
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
                        value={cuisines.value ? cuisines.value : []}
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
                <UploadPhotos
                    setImages={setImages}
                    previewImages={previewImages}
                    setPreviewImages={setPreviewImages}
                    uploadDescription={"Upload a Photo"}
                />

                <Box pt={3} pb={3}>
                    <ButtonStyled
                        variant="contained"
                        color="primary"
                        endIcon={<SendIcon />}
                        onClick={submitForm}
                    >
                        {isRegister ? "Register" : "Update"}
                    </ButtonStyled>
                </Box>
            </FloatBox>
        </AlignCenter>
  );
}
