import React, { useState, useEffect, useContext } from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";
import { ProfilePhoto } from '../styles/ProfilePhoto'
import { Box, Button, Divider, Dialog, DialogTitle, DialogContent, DialogActions, TextField} from "@material-ui/core";
import { Label } from "../styles/Label";
import { FileUpload } from "../styles/FileUpload";
import { StatBox } from "../styles/StatBox";
import EditIcon from '@material-ui/icons/Edit';
import AddAPhoto from "@material-ui/icons/AddAPhoto";
import Review from "../components/Review";
import { fileToDataUrl, validRequired, validEmail, validPassword, validConfirmPassword } from "../utils/helpers";
import { StoreContext } from "../utils/store";

export default function DinerProfile({ token }) {

  const context = useContext(StoreContext);
  const setAlertOptions = context.alert[1];

  const [openProfile, setOpenProfile] = useState(false);

  const defaultState = (initialValue = "") => {
    return { value: initialValue, valid: true }
  };

  const [username, setUsername] = useState(defaultState);
  const [email, setEmail] = useState(defaultState);
  const [password, setPassword] = useState(defaultState);
  const [confirmpassword, setConfirmpassword] = useState(defaultState);
  const [tmpProfilePic, setTmpProfilePic] = useState(defaultState);
  const [user, setUser] = useState("");

  useEffect(() => {
    // on page init, load the users details
    const getUser = async () => {
      const response = await fetch(
        "http://localhost:8080/diner/profile/details",
        {
          method: "GET",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
            Authorization: token,
          },
        }
      );
      const responseData = await response.json();
      if (response.status === 200) {
          console.log(responseData);
          setUser({
            "username": responseData.name,
            "email": responseData.email,
            "profilePic": responseData["profile picture"],
            "reviews": responseData.reviews
          })
          setUsername(defaultState(responseData.name));
          setEmail(defaultState(responseData.email));
          setTmpProfilePic(responseData["profile picture"]);
          // setEateryList(responseData.eateryList);
      }
    };
    getUser();
  }, []);

  const handleClose = () => {
    setOpenProfile(false);
    setTmpProfilePic(user.profilePic);
  }

  const saveChanges = async () => {
    console.log("changes are going");
    // Ideally this form should be validated when a form is submitted
    if (user.username.value === "") setUsername({value: "", valid: false});
    if (user.email.value === "") setEmail({value: "", valid: false});
    // check that all fields are valid and not empty before registering
    if (
      !username.valid ||
      !email.valid ||
      !password.valid ||
      !confirmpassword.valid ||
      username.value === "" ||
      email.value === ""
    ) return;
    const response = await fetch(
      "http://localhost:8080/update/diner",
      {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: token,
        },
        body: JSON.stringify({
          "email": email.value,
          "password": (password.value.length !== 0 ? password.value : null),
          "alias": username.value,
          "profilePic": tmpProfilePic
        })
      }
    );
    const responseData = await response.json();
    if (response.status === 200) {
        console.log(responseData);
        setUser({ ...user,
          "username": username.value,
          "email": email.value,
          "profilePic": tmpProfilePic,
        })
        setOpenProfile(false);
        setAlertOptions({ showAlert: true, variant: 'success', message: responseData.message });
    }
  }

  const handleImage = (data) => {
    Array.from(data).forEach((file) => {
        fileToDataUrl(file).then((url) => {
          setTmpProfilePic(url);
            // setImages((prevArray) => [...prevArray, url]);
        });
    });
  };

  return (
    <>
      <NavBar token={token} isDiner={true}/>
      <MainContent>
        <Box display="flex" justifyContent="center" alignItems="center" paddingTop="10px">
          <ProfilePhoto size={150} src={user.profilePic} />
          <Box display="flex" flexDirection="column" alignItems="center" paddingX="20px">
            <h1>{user.username}</h1>
            <Button variant="contained"
              color="primary"
              startIcon={<EditIcon />}
              onClick={() => setOpenProfile(true)}>
              Edit profile
            </Button>
          </Box>
          <StatBox>
            <h1>5</h1>
            <h1>reviews</h1>
          </StatBox>
          <StatBox>
            <h1>4</h1>
            <h1>photos</h1>
          </StatBox>
        </Box>
        <Box paddingY="30px">
          <Divider variant="middle" />
        </Box>
        {/* Reviews would be mapped here... */}
        <Box display="flex" flexDirection="column" flexGrow="1" alignItems="center" style={{overflowY: 'scroll', height: "auto"}}>
          <Review token={token} user={user} eateryName="Test Eatery" review={"This is a sample review in the future we will use real reviews from diners"} rating={3.5}></Review>
          <Review token={token} user={user} eateryName="Test Eatery" review={"This is a sample review in the future we will use real reviews from diners"} rating={3.5}></Review>
          <Review token={token} user={user} eateryName="Test Eatery" review={"This is a sample review in the future we will use real reviews from diners"} rating={3.5}></Review>
          <Review token={token} user={user} eateryName="Test Eatery" review={"This is a sample review in the future we will use real reviews from diners"} rating={3.5}></Review>
          <Review token={token} user={user} eateryName="Test Eatery" review={"This is a sample review in the future we will use real reviews from diners"} rating={3.5}></Review>
        </Box>
        <Dialog aria-labelledby="customized-dialog-title" open={openProfile}>
          <DialogTitle id="customized-dialog-title">
            Update Profile
          </DialogTitle>
          <DialogContent dividers>
            <Box pt={1} display="flex">
              <ProfilePhoto size={70} src={tmpProfilePic}></ProfilePhoto>
              <Box pt={2}>
                <Label>
                  <FileUpload
                    type="file"
                    onChange={(e) => handleImage(e.target.files)}
                  />
                  {<AddAPhoto />} Change Profile Picture
                </Label>
                </Box>
            </Box>
            <Box pt={1}>
              <TextField
                  id="outlined-basic"
                  label="Username"
                  onChange={(e) =>
                    setUsername({ value: e.target.value, valid: true })
                  }
                  onBlur={() => validRequired(username, setUsername)}
                  error={!username.valid}
                  helperText={
                      username.valid ? "" : "Please enter a username"
                  }
                  value={username.value}
                  variant="outlined"
                  fullWidth
              />
            </Box>
            <Box pt={2}>
                <TextField
                    id="outlined-basic"
                    label="Email Address"
                    onChange={(e) =>
                      setEmail({ value: e.target.value, valid: true })
                    }
                    onBlur={() => validEmail(email, setEmail)}
                    error={!email.valid}
                    helperText={
                        email.valid ? "" : "Please enter a valid email"
                    }
                    value={email.value}
                    variant="outlined"
                    fullWidth
                />
            </Box>
            <Box pt={2}>
              <TextField
                id="outlined-basic"
                label="Password"
                type="password"
                onChange={(e) =>
                  setPassword({ value: e.target.value, valid: true })
                }
                onBlur={() => validPassword(password, setPassword)}
                error={!password.valid && password.value.length !== 0}
                helperText={
                  (password.valid || password.value.length === 0)
                    ? ""
                    : "Please enter a valid password with 1 lowercase, 1 upper case, 1 number with at least 8 characters"
                }
                variant="outlined"
                fullWidth
              />
            </Box>
            <Box pt={2}>
              <TextField
                id="outlined-basic"
                label="Confirm Password"
                type="password"
                onChange={(e) =>
                  setConfirmpassword({
                    value: e.target.value,
                    valid: true,
                  })
                }
                onBlur={() => { validConfirmPassword(password, confirmpassword, setConfirmpassword); console.log(confirmpassword);}}
                error={!confirmpassword.valid}
                helperText={
                  confirmpassword.valid
                    ? ""
                    : "Please make sure your passwords match"
                }
                variant="outlined"
                fullWidth
              />
            </Box>
          </DialogContent>
          <DialogActions>
            <Button autoFocus onClick={handleClose} color="primary">
              Cancel
            </Button>
            <Button autoFocus onClick={saveChanges} color="primary">
              Save changes
            </Button>
          </DialogActions>
        </Dialog>
      </MainContent>
    </>
  );
}
