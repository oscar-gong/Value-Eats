import { render, shallow } from "enzyme";
import React from "react";
import renderer from "react-test-renderer";
import StarRating from "../components/StarRating";

describe("StarRating", () => {
  const noop = () => {};

  it("can work in the base case", () => {
    const rating = shallow(<StarRating rating={5} setRating={noop}/>);
    expect(rating.exists()).toBeTruthy();
    expect(rating.find("span")).toBeTruthy();
  });

  it("can handle clicks correctly when editable", () => {
    const onClick = jest.fn();
    const rating = shallow(<StarRating rating={5} isEditable={true} setRating={onClick}/>);
    // console.log(rating.debug({ verbose: true }));
    rating.simulate("change");
    expect(onClick).toHaveBeenCalledTimes(1);
  });

  it("can handle isEditable true correctly", () => {
    const rating = render(<StarRating rating={5} isEditable={false} setRating={noop}/>);
    expect(rating.hasClass("MuiRating-readOnly")).toBeTruthy();
  });

  it("can handle isEditable false correctly", () => {
    const rating = render(<StarRating rating={5} isEditable={true} setRating={noop}/>);
    // expect(rating.hasClass("MuiRating-readOnly")).toBeTruthy();
    expect(rating.hasClass("MuiRating-readOnly")).toBeFalsy();
  });

  /* Snapshot tests - creates snapshots for new tests, otherwise will check the existing tests. To update existing tests,
  follow the prompts on the screen after running test. Done to avoid regression. */

  it("renders with minimal props", () => {
    const rating = renderer.create(<StarRating rating={5} setRating={noop}/>);
    expect(rating).toMatchSnapshot();
  });

  it("renders with double star rating", () => {
    const rating = renderer.create(<StarRating rating={2.5} setRating={noop}/>);
    expect(rating).toMatchSnapshot();
  });

  it("renders correctly when editable", () => {
    const rating = renderer.create(<StarRating rating={5} isEditable={true} setRating={noop}/>);
    expect(rating).toMatchSnapshot();
  });
});
