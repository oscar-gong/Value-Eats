import { shallow } from "enzyme";
import React from "react";
import renderer from "react-test-renderer";
import UploadPhotos from "../components/UploadPhotos";

describe("UploadPhotos", () => {
  const noop = () => {};

  it("can work in the base case", () => {
    const upload = shallow(<UploadPhotos setImages={noop} previewImages={[]} setPreviewImages={noop} uploadDescription={"Upload"}/>);
    // console.log(upload.debug({ verbose: true }));
    expect(upload.exists()).toBeTruthy();
  });

  it("text is on the upload element", () => {
    const upload = shallow(<UploadPhotos setImages={noop} previewImages={[]} setPreviewImages={noop} uploadDescription={"Upload"}/>);
    expect(upload.text()).toBe("Upload");
    const uploadImages = shallow(<UploadPhotos setImages={noop} previewImages={[]} setPreviewImages={noop} uploadDescription={"Upload Images"}/>);
    expect(uploadImages.text()).toBe("Upload Images");
  });

  //   it("can handle the preview images being changed", () => {
  //     const onClick = jest.fn();
  //     const upload = shallow(<UploadPhotos setImages={onClick} previewImages={[]} setPreviewImages={onClick} uploadDescription={"Upload"}/>);
  //     upload.simulate("change", {
  //       target: {
  //         files: [
  //           "dummyValue.something"
  //         ]
  //       }
  //     });
  //     expect(onClick).toHaveBeenCalledTimes(1);
  //   });

  // Snapshot tests
  it("renders with minimal props", () => {
    const rating = renderer.create(<UploadPhotos setImages={noop} previewImages={[]} setPreviewImages={noop} uploadDescription={"Upload"}/>);
    expect(rating).toMatchSnapshot();
  });

  it("renders with images", () => {
    const previewImages = [{ img: "", id: 0 }, { img: "", id: 1 }];
    const upload = renderer.create(<UploadPhotos setImages={noop} previewImages={previewImages} setPreviewImages={noop} uploadDescription={"Upload"}/>);
    expect(upload).toMatchSnapshot();
  });
});
